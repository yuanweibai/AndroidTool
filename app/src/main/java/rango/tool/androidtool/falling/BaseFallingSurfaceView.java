package rango.tool.androidtool.falling;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFallingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "FallingSurfaceView";

    private static final float RATION_REAL_UPDATE_INTERVAL = 0.75f;
    private static final long INTERVAL_UPDATE_DRAW = 16;
    private static final int DEFAULT_MIN_FALLING_SPEED = 4;

    public List<BaseFallingBean> fallingBeanList;
    public int contentAlpha;

    private long lastUpdateTime;
    private long logLastDrawEndTime;
    private long logLastCallUpdateTime;

    private ValueAnimator mValueAnimator;

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

    protected abstract void createFallingItems(int minFallingSpeed);

    protected abstract void onSurfaceViewDraw(Canvas canvas);

    public void startFallingAnim() {
        startFallingAnim(false, 0, DEFAULT_MIN_FALLING_SPEED);
    }

    public void startFallingAnim(long duration, int minFallingSpeed, float startAlpha) {
        startFallingAnim(true, duration, minFallingSpeed);

        startAlphaAnim(startAlpha, duration);
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
        releaseAnim();
    }

    private void initView() {
        setZOrderOnTop(true);
        contentAlpha = 255;

        bgPaint = new Paint();
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        fallingBeanList = new ArrayList<>();

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    private void updateSurfaceView() {
        synchronized (this) {
            if (handler == null) {
                return;
            }

            handler.post(this::surfaceViewDraw);
        }
    }

    private void surfaceViewDraw() {
        if (!isCouldDraw) {
            return;
        }

        if (getVisibility() != SurfaceView.VISIBLE) {
            return;
        }

        SurfaceHolder surfaceHolder = getHolder();
        if (surfaceHolder == null || surfaceHolder.isCreating()) {
            return;
        }

        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (canvas == null) {
            return;
        }

        // clear bg
        canvas.drawPaint(bgPaint);

        final long drawStartTime = System.currentTimeMillis();

        onSurfaceViewDraw(canvas);

        final long drawEndTime = System.currentTimeMillis();

        Log.d(TAG, "init background time is " + (drawStartTime - logLastCallUpdateTime)
                + ", draw falling items time is " + (drawEndTime - drawStartTime)
                + ", call draw interval time is " + (drawEndTime - logLastDrawEndTime));

        logLastDrawEndTime = System.currentTimeMillis();

        try {
            surfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mUpdateFallingItemsRunnable = new Runnable() {

        @Override
        public void run() {

            final long intervalUpdate = System.currentTimeMillis() - lastUpdateTime;
            final float coefficient = (float) ((double) intervalUpdate / INTERVAL_UPDATE_DRAW);

            for (BaseFallingBean bean : fallingBeanList) {
                bean.updateData(BaseFallingSurfaceView.this.getWidth(), BaseFallingSurfaceView.this.getHeight(), coefficient);
            }

            logLastCallUpdateTime = System.currentTimeMillis();
            updateSurfaceView();

            lastUpdateTime = System.currentTimeMillis();

            synchronized (BaseFallingSurfaceView.this) {
                if (handler != null) {
                    handler.postDelayed(this, (long) (INTERVAL_UPDATE_DRAW * (1 - RATION_REAL_UPDATE_INTERVAL)
                            + intervalUpdate * RATION_REAL_UPDATE_INTERVAL));
                }
            }
        }
    };

    private Runnable mRemoveFallingItemsUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            release();

            if (handler == null) {
                return;
            }
            handler.removeCallbacks(mUpdateFallingItemsRunnable);

            logLastCallUpdateTime = System.currentTimeMillis();
            updateSurfaceView();
        }
    };

    private void startFallingAnim(boolean isAutoStop, long duration, int minFallingSpeed) {
        if (!isCouldDraw()) {
            return;
        }
        handler.removeCallbacks(mRemoveFallingItemsUpdateRunnable);
        if (isAutoStop) {
            handler.postDelayed(mRemoveFallingItemsUpdateRunnable, duration);
        }

        lastUpdateTime = System.currentTimeMillis();

        handler.removeCallbacks(mUpdateFallingItemsRunnable);
        handler.post(() -> createFallingItems(minFallingSpeed));
        handler.post(mUpdateFallingItemsRunnable);
    }

    private void startAlphaAnim(float startAlpha, long duration) {
        releaseAnim();

        mValueAnimator = ValueAnimator.ofFloat(startAlpha, 1f, 1f, 0f);
        mValueAnimator.addUpdateListener(animation -> contentAlpha = (int) (255 * (float) animation.getAnimatedValue()));
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }

    private void releaseAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }
}
