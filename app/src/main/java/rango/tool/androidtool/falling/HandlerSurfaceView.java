package rango.tool.androidtool.falling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class HandlerSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = HandlerSurfaceView.class.getSimpleName();

    private HandlerThread mHandlerThread;

    @Nullable
    protected Handler mHandler;

    // Clear background
    private Paint mPaint;
    private boolean mDrawOk;

    public HandlerSurfaceView(Context context) {
        this(context, null);
    }

    public HandlerSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandlerSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());
        }

        onSurfaceCreated(holder);
        mDrawOk = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");

        onSurfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");

        mDrawOk = false;
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mHandlerThread != null){
            mHandlerThread.quit();
            mHandlerThread = null;
        }

        onSurfaceDestroyed(holder);
    }

    public void updateSurfaceView(final boolean isClearCanvas) {
        synchronized (this) {
            if (mHandler == null) {
                return;
            }

            mHandler.post(() -> surfaceViewDraw(isClearCanvas));
        }
    }

    private void init() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void surfaceViewDraw(final boolean isClearCanvas) {
        synchronized (this) {
            if (!mDrawOk) {
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

            if (isClearCanvas) {
                canvas.drawPaint(mPaint);
            }
            onSurfaceViewDraw(canvas);

            try {
                surfaceHolder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void onSurfaceCreated(SurfaceHolder holder);

    public abstract void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);

    public abstract void onSurfaceDestroyed(SurfaceHolder holder);

    public abstract void onSurfaceViewDraw(Canvas canvas);
}
