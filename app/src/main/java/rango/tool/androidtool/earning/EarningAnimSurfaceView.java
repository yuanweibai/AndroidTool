package rango.tool.androidtool.earning;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.WorkerThread;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

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
            bitmap = BitmapFactory.decodeResource(ToolApplication.getContext().getResources(), R.drawable.animate_coin_icon);
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        earningBeanList.clear();

        long[] scaleDurationArray = new long[]{420, 400, 400};
//        float[][] scaleArray = new float[][]{{0.4f, 1.1f, 0.95f, 1f}, {0.4f, 0.98f, 0.85f, 0.90f}, {0.4f, 0.85f, 0.76f, 0.8f}};
        float[][] firstScaleArray = new float[][]{{0.4f, 1.1f}, {0.4f, 0.98f}, {0.4f, 0.85f}};
        int[][] firstTranslateDis = new int[][]{{0, 0}, {11, 27}, {12, -39}, {27, 0}, {47, -20}, {64, 0}, {-21, -24}, {-59, 0}, {-27, 13}};
        int[][] firstRotationArray = new int[][]{{0, 0, 0}, {0, 10, 0}, {0, 10, 0}, {0, 15, 0}, {0, 10, 0}, {0, 20, 0}, {0, -10, 0}, {0, -20, 0}, {0, -15, 0}};
        int[] firstRotationDelayArray = new int[]{0, 40, 40, 0, 40, 80, 0, 0, 0};

        float[][] secondScaleArray = new float[][]{{1.1f, 0.95f, 1f}, {0.98f, 0.85f, 0.9f}, {0.85f, 0.76f, 0.80f}};
        TimeInterpolator interpolator = PathInterpolatorCompat.create(0.17f, 0.17f, 0.67f, 1f);
        for (int i = 0; i < 9; i++) {
            int x = startX - halfWidth;
            int y = startY - halfHeight;
            EarningBean bean = new EarningBean(x, y, endX, endY);

            int temp = i % 3;
            bean.setFirstDuration(200);
            bean.setFirstScaleArray(firstScaleArray[temp]);
            bean.setFirstTranslation(firstTranslateDis[i][0], firstTranslateDis[i][1]);
            bean.setInterpolator(interpolator);

            bean.setFirstRotationArray(firstRotationArray[i]);
            bean.setFirstRotationDelay(firstRotationDelayArray[i]);
            bean.setFirstRotationDuration(280);

            bean.setSecondScaleArray(secondScaleArray[temp]);
            bean.setSecondDuration(200);

            bean.setThirdDelay(150);
            bean.setThirdDisRotation(180);
            bean.setThirdEndScale(0.4f);
            bean.setThirdDuration(320);

            bean.start(temp * 40);

            earningBeanList.add(bean);
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
            matrix.setTranslate(bean.getX(), bean.getY());
            matrix.preScale(bean.getScale(), bean.getScale());
            matrix.preRotate(bean.getRotation());

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
            if (!isStart) {
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
