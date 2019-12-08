package rango.tool.androidtool.falling;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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

public abstract class FallingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "FallingSurfaceView";

    private static final float RATION_REAL_UPDATE_INTERVAL = 0.75f;
    private static final long INTERVAL_UPDATE_DRAW = 16;
    private static final int DEFAULT_MIN_FALLING_SPEED = 4;

    protected Matrix contentMatrix;
    private int contentAlpha;
    protected Paint contentPaint;

    protected List<FallingPathItem> fallingItems;

    private long lastUpdateTime;

    private long logLastDrawEndTime;
    private long logLastCallUpdateTime;

    private ValueAnimator mValueAnimator;

    private HandlerThread handlerThread;
    private Handler handler;

    // Clear background
    private Paint bgPaint;

    private boolean isCouldDraw;

    public FallingSurfaceView(Context context) {
        this(context, null);
    }

    public FallingSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected abstract void createFallingItems(int minFallingSpeed);

    protected abstract void onSurfaceViewDraw(Canvas canvas, FallingPathItem fallingItem);

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
        Log.d(TAG, "surfaceCreated()");
        if (handlerThread == null) {
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
        isCouldDraw = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");

        isCouldDraw = false;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }
    }

    private void init() {

        setZOrderOnTop(true);

        contentPaint = new Paint();
        contentMatrix = new Matrix();

        fallingItems = new ArrayList<>();

        contentAlpha = 255;

        bgPaint = new Paint();
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);

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
        synchronized (this) {
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

            for (FallingPathItem fallingItem : fallingItems) {

                if (fallingItem.posX < 0 || fallingItem.posX > getWidth()) {
                    continue;
                }

                contentMatrix.setRotate(fallingItem.rotateAngle);
                contentMatrix.postScale(fallingItem.scaleRatio, fallingItem.scaleRatio);
                contentMatrix.postTranslate(fallingItem.posX, fallingItem.posY);

                contentPaint.setColor(fallingItem.color);
                contentPaint.setAlpha((int) (contentAlpha * fallingItem.alpha));

                onSurfaceViewDraw(canvas, fallingItem);
            }

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
    }

    private Runnable mUpdateFallingItemsRunnable = new Runnable() {

        @Override
        public void run() {

            final long intervalUpdate = System.currentTimeMillis() - lastUpdateTime;
            final float coefficient = (float) ((double) intervalUpdate / INTERVAL_UPDATE_DRAW);

            for (FallingPathItem fallingItem : fallingItems) {
                fallingItem.updatePosition(FallingSurfaceView.this.getWidth(), FallingSurfaceView.this.getHeight(), coefficient);
            }

            logLastCallUpdateTime = System.currentTimeMillis();
            updateSurfaceView();

            lastUpdateTime = System.currentTimeMillis();

            synchronized (FallingSurfaceView.this) {
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
            if (handler == null) {
                return;
            }
            handler.removeCallbacks(mUpdateFallingItemsRunnable);

            fallingItems.clear();

            logLastCallUpdateTime = System.currentTimeMillis();
            updateSurfaceView();

            releaseAnim();
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
