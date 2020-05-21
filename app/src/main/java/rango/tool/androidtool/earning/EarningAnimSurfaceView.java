package rango.tool.androidtool.earning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.WorkerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;

public class EarningAnimSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "EarningAnimSurfaceView";

    private int startX;
    private int startY;

    private int endX;
    private int endY;

    private Bitmap bitmap;

    private HandlerThread handlerThread;
    private Handler handler;

    private boolean isCouldDraw = false;

    private final Object lock = new Object();

    private Runnable waitRunnable;

    private List<EarningBean> earningBeanList = new ArrayList<>();

    private SurfaceHolder surfaceHolder;

    private Paint bgPaint;
    private Paint contentPaint;
    private Matrix matrix;

    private volatile boolean isStart = false;

    public EarningAnimSurfaceView(Context context) {
        this(context, null);
    }

    public EarningAnimSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EarningAnimSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void setStart(int x, int y) {
        startX = x;
        startY = y;
    }

    public void setEnd(int x, int y) {
        endX = x;
        endY = y;
    }

    public void start() {
        isStart = true;
        if (isCouldDraw) {
            synchronized (lock) {
                handler.post(this::createCoinItems);
                handler.post(updateEarningAnimRunnable);
            }
        } else {
            waitRunnable = this::start;
        }
    }

    public void stop() {
        isStart = false;
        synchronized (lock) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }

    }

    private void init() {
        setZOrderOnTop(true);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setStyle(Paint.Style.FILL);

        matrix = new Matrix();

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    @WorkerThread
    private void createCoinItems() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(ToolApplication.getContext().getResources(), R.drawable.coin_icon);
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        earningBeanList.clear();

        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                EarningBean bean = new EarningBean(startX + i * width, startY + k * height,
                        endX, endY);
                bean.start((i*4+k) * 20);

                earningBeanList.add(bean);
            }
        }
    }

    @WorkerThread
    private void onSurfaceDraw() {

        Log.e(TAG, "onSurfaceDraw, size = " + earningBeanList.size());
        if (surfaceHolder == null || surfaceHolder.isCreating()) {
            return;
        }

        Canvas canvas;
        try {
            canvas = surfaceHolder.lockCanvas();
        } catch (Exception ignore) {
            return;
        }

        if (canvas == null) {
            return;
        }

        if (earningBeanList.isEmpty()) {
            return;
        }

        canvas.drawPaint(bgPaint);

        for (EarningBean bean : earningBeanList) {
            matrix.reset();
            matrix.postTranslate(bean.getX(), bean.getY());
            matrix.preScale(bean.getScale(), bean.getScale());

            canvas.drawBitmap(bitmap, matrix, contentPaint);
        }

        try {
            surfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Exception ignored) {
        }
    }

    private Runnable updateEarningAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isStart){
                return;
            }

            onSurfaceDraw();

            synchronized (lock) {
                handler.post(this);
            }
        }
    };


    private void initHandler() {
        if (handler == null) {
            handlerThread = new HandlerThread("EarningAnim");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initHandler();
        isCouldDraw = true;
        if (waitRunnable != null) {
            waitRunnable.run();
            waitRunnable = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCouldDraw = false;
        synchronized (lock) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler.post(releaseRunnable);
            }
        }
    }

    private Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {
            if (handlerThread != null) {
                handlerThread.quit();
                handlerThread = null;
            }
        }
    };
}
