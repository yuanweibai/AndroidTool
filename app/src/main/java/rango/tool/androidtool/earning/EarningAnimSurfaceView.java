package rango.tool.androidtool.earning;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import rango.tool.common.utils.Worker;

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

    private float bitmapPx = 0f;
    private float bitmapPy = 0f;

    private volatile boolean isStart = false;

    private EarningAnimEndListener earningAnimEndListener = new EarningAnimEndListener() {
        private int count;

        @Override
        @WorkerThread
        public void onAnimEnd() {
            count++;
            Log.e(TAG, "count = " + count);
            if (count >= earningBeanList.size()) {
                stop();
                count = 0;
                onSurfaceDraw();

                Worker.postMain(() -> {
                    if (allEarningEndListener != null) {
                        allEarningEndListener.onAnimEnd();
                    }
                });
            }
        }
    };

    private EarningAnimEndListener allEarningEndListener;

    public EarningAnimSurfaceView(Context context, int startX, int startY, int endX, int endY) {
        super(context);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        init();
    }

    public void setAllEarningEndListener(EarningAnimEndListener earningEndListener) {
        this.allEarningEndListener = earningEndListener;
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
        contentPaint.setColor(Color.RED);

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

        bitmapPx = halfWidth;
        bitmapPy = halfHeight;

        int sX = startX - halfWidth;
        int sY = startY - halfHeight;

        int eX = endX - halfWidth;
        int eY = endY - halfHeight;

        earningBeanList.clear();

        float[][] firstScaleArray = new float[][]{{0.4f, 1.1f}, {0.4f, 0.98f}, {0.4f, 0.85f}};
        int[][] firstTranslateDisDp = new int[][]{{0, 0}, {11, 27}, {12, -39}, {27, 0}, {47, -20}, {64, 0}, {-21, -24}, {-59, 0}, {-27, 13}};
        int[][] firstRotationArray = new int[][]{{0, 0, 0}, {0, 10, 0}, {0, 10, 0}, {0, 15, 0}, {0, 10, 0}, {0, 20, 0}, {0, -10, 0}, {0, -20, 0}, {0, -15, 0}};
        int[] firstRotationDelayArray = new int[]{0, 40, 40, 0, 40, 80, 0, 0, 0};

        float[][] secondScaleArray = new float[][]{{1.1f, 0.95f, 1f}, {0.98f, 0.85f, 0.9f}, {0.85f, 0.76f, 0.80f}};
        TimeInterpolator interpolator = PathInterpolatorCompat.create(0.17f, 0.17f, 0.67f, 1f);
        for (int i = 0; i < 9; i++) {
            int index = i % 3;
            EarningBean bean = new EarningBean.Builder()
                    .setStartPointer(sX, sY)
                    .setEndPointer(eX, eY)
                    .setInterpolator(interpolator)
                    .setFirstAnim(firstRotationArray[i], firstRotationDelayArray[i], 280, firstTranslateDisDp[i][0], firstTranslateDisDp[i][1], firstScaleArray[index], 200)
                    .setSecondAnim(secondScaleArray[index], 200)
                    .setLastAnim(0.4f, 180, 320, 150)
                    .build();
            bean.setEarningAnimEndListener(earningAnimEndListener);
            bean.start(index * 40);

            earningBeanList.add(bean);
        }
    }

    @WorkerThread
    private void onSurfaceDraw() {

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
        canvas.drawPaint(bgPaint);

        if (earningBeanList.isEmpty()) {
            return;
        }

        for (EarningBean bean : earningBeanList) {
            matrix.reset();
            matrix.setTranslate(bean.getX(), bean.getY());
            matrix.preScale(bean.getScale(), bean.getScale(), bitmapPx, bitmapPy);
            matrix.preRotate(bean.getRotation(), bitmapPx, bitmapPy);

            canvas.drawBitmap(bitmap, matrix, contentPaint);
        }

        canvas.drawCircle(endX, endY, 2, contentPaint);

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
        Log.e(TAG, "surfaceCreated");
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
        Log.e(TAG, "surfaceDestroyed");
        isCouldDraw = false;
        release();
    }

    private void release() {
        isStart = false;
        synchronized (lock) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }

            if (handlerThread != null) {
                handlerThread.quit();
                handlerThread = null;
            }
        }
    }
}
