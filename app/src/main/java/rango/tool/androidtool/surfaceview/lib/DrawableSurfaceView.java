package rango.tool.androidtool.surfaceview.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.WorkerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import rango.tool.common.utils.TouchEventUtils;

public class DrawableSurfaceView extends SurfaceView implements SurfaceHolder.Callback2, IDrawableAction {

    private static final String TAG = "PigSurfaceView";
    private static final String THREAD_TAG = TAG + "_Thread";
    private static final String DRAW_TAG = TAG + "_Draw";
    private static final String NORMAL_TAG = TAG + "_Normal";

    private final Object lock = new Object();

    private HandlerThread handlerThread;
    private Handler handler;

    private boolean isCouldDraw = false;
    private volatile boolean isDrawStart = false;
    private Runnable waitRunnable = null;
    private SurfaceHolder surfaceHolder;
    private List<IDrawableBean> drawBeanList;

    private Paint clearPaint;

    public DrawableSurfaceView(Context context) {
        this(context, null);
    }

    public DrawableSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    @Override
    public void update(List<? extends IDrawableBean> drawBeans) {
        synchronized (lock) {
            drawBeanList.clear();
            drawBeanList.addAll(drawBeans);
        }
    }

    @Override
    public void append(IDrawableBean drawBean) {
        synchronized (lock) {
            drawBeanList.add(drawBean);
        }
    }

    @Override
    public void append(List<? extends IDrawableBean> drawBeans) {
        synchronized (lock) {
            drawBeanList.addAll(drawBeans);
        }
    }

    @Override
    public void remove(IDrawableBean drawableBean) {
        synchronized (lock) {
            drawBeanList.remove(drawableBean);
        }
    }

    public void onStart() {
        Log.d(NORMAL_TAG, "onStart......");
        isDrawStart = true;
        if (isCouldDraw) {
            startDraw();
        } else {
            waitRunnable = this::onStart;
        }
    }

    public void onStop() {
        Log.d(NORMAL_TAG, "onStop......");
        stopDraw();
    }

    public void onDestroy() {
        Log.d(NORMAL_TAG, "onDestroy......");
        stopThread();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(NORMAL_TAG, "surfaceCreated......");
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
        Log.d(NORMAL_TAG, "surfaceDestroyed......");
        isCouldDraw = false;
    }

    private float downX;
    private float downY;
    private IDrawableBean selectDrawBean;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int pointerId = TouchEventUtils.getDefaultPointerId(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = TouchEventUtils.getX(event, pointerId);
                downY = TouchEventUtils.getY(event, pointerId);
                return onStartMove();
            case MotionEvent.ACTION_MOVE:
                float moveX = TouchEventUtils.getX(event, pointerId);
                float moveY = TouchEventUtils.getY(event, pointerId);
                onMoving(moveX - downX, moveY - downY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float upX = TouchEventUtils.getX(event, pointerId);
                float upY = TouchEventUtils.getY(event, pointerId);
                onEndMove(upX, upY, upX - downX, upY - downY);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean onStartMove() {
        selectDrawBean = null;
        for (IDrawableBean bean : drawBeanList) {
            if (bean.isInterceptTouchEvent() && bean.onActionDown(downX, downY)) {
                selectDrawBean = bean;
                break;
            }
        }
        return selectDrawBean != null;
    }

    private void onMoving(float disX, float disY) {
        if (selectDrawBean != null) {
            selectDrawBean.onActionMove(disX, disY);
        }
    }

    private void onEndMove(float upX, float upY, float disX, float disY) {
        if (selectDrawBean != null) {
            selectDrawBean.onActionUp(upX, upY, disX, disY);
            selectDrawBean = null;
        }
    }

    private void init() {
        initHandlerThread();
        drawBeanList = new ArrayList<>();
        setZOrderOnTop(true);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isDrawStart) {
                Log.d(NORMAL_TAG, "drawRunnable: isDrawStart is false!!!");
                return;
            }

            onSurfaceDraw();

            synchronized (lock) {
                if (handler != null) {
                    handler.post(this);
                }
            }
        }
    };

    private void startDraw() {
        synchronized (lock) {
            for (IDrawableBean bean : drawBeanList) {
                bean.start();
            }

            if (handler != null) {
                handler.post(drawRunnable);
            }
        }
    }

    @WorkerThread
    private void onSurfaceDraw() {
        Log.d(DRAW_TAG, "onSurfaceDraw()....");

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

        synchronized (lock) {
//            canvas.drawPaint(clearPaint);
            for (IDrawableBean bean : drawBeanList) {
                if (bean != null) {
                    bean.onDraw(canvas);
                }
            }
        }

        try {
            surfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Exception ignore) {
        }
    }

    private void initHandlerThread() {
        synchronized (lock) {
            if (handlerThread == null) {
                handlerThread = new HandlerThread(THREAD_TAG);
                handlerThread.start();
                handler = new Handler(handlerThread.getLooper());
            }
        }
    }

    private void stopDraw() {
        isDrawStart = false;
        synchronized (lock) {
            for (IDrawableBean bean : drawBeanList) {
                bean.stop();
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    private void stopThread() {
        synchronized (lock) {
            if (handlerThread != null) {
                handlerThread.quit();
                handlerThread = null;
                handler = null;
            }
        }
    }
}
