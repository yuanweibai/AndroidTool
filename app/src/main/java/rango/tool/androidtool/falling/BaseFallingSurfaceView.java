package rango.tool.androidtool.falling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFallingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "FallingSurfaceView";

    private static final int DEFAULT_MIN_FALLING_SPEED = 4;

    public List<BaseFallingBean> fallingBeanList;

    private HandlerThread handlerThread;
    private Handler handler;

    // Clear background
    private Paint bgPaint;

    private boolean isCouldDraw;

    public BaseFallingSurfaceView(Context context) {
        this(context, null);
    }

    public BaseFallingSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFallingSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected abstract void createFallingItems(int fallingSpeed);

    protected abstract boolean onSurfaceViewDraw(Canvas canvas);

    public void startFallingAnim() {
        startFallingAnim(false, 0, DEFAULT_MIN_FALLING_SPEED);
    }

    public void startFallingAnim(long duration, int fallingSpeed) {
        startFallingAnim(true, duration, fallingSpeed);
    }

    public boolean isCouldDraw() {
        return isCouldDraw;
    }

    public void stopFallingAnim() {
        if (handler != null) {
            handler.removeCallbacks(mRemoveFallingItemsUpdateRunnable);
            handler.post(mRemoveFallingItemsUpdateRunnable);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (handlerThread == null) {
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
        isCouldDraw = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCouldDraw = false;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }

        release();
    }

    private void release() {
        fallingBeanList.clear();
    }

    private void initView() {
        setZOrderOnTop(true);

        bgPaint = new Paint();
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        fallingBeanList = new ArrayList<>();

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    private boolean surfaceViewDraw() {

        boolean shouldContinueDraw;
        if (!isCouldDraw) {
            return false;
        }

        if (getVisibility() != SurfaceView.VISIBLE) {
            return false;
        }

        SurfaceHolder surfaceHolder = getHolder();
        if (surfaceHolder == null || surfaceHolder.isCreating()) {
            return false;
        }

        Canvas canvas;
        try {
            canvas = surfaceHolder.lockCanvas();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (canvas == null) {
            return false;
        }

        // clear bg
        canvas.drawPaint(bgPaint);

        shouldContinueDraw = onSurfaceViewDraw(canvas);

        try {
            surfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shouldContinueDraw;
    }

    private Runnable mUpdateFallingItemsRunnable = new Runnable() {

        @Override
        public void run() {

            for (BaseFallingBean bean : fallingBeanList) {
                bean.updateData(BaseFallingSurfaceView.this.getWidth(), BaseFallingSurfaceView.this.getHeight());
            }

            // Is continue to draw next?
            boolean isPostNextDraw = surfaceViewDraw();

            if (isPostNextDraw) {
                if (handler != null) {
                    handler.post(this);
                }
            } else {
                release();
            }
        }
    };

    private Runnable mRemoveFallingItemsUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            for (BaseFallingBean bean : fallingBeanList) {
                bean.notifyEnd();
            }
        }
    };

    private void startFallingAnim(boolean isAutoStop, long duration, int fallingSpeed) {
        if (!isCouldDraw()) {
            return;
        }
        handler.removeCallbacks(mRemoveFallingItemsUpdateRunnable);
        if (isAutoStop) {
            handler.postDelayed(mRemoveFallingItemsUpdateRunnable, duration);
        }

        handler.removeCallbacks(mUpdateFallingItemsRunnable);
        handler.post(() -> createFallingItems(fallingSpeed));
        handler.post(mUpdateFallingItemsRunnable);
    }
}
