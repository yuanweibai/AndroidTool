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

    private HandlerThread handlerThread;

    @Nullable
    protected Handler handler;

    // Clear background
    private Paint paint;
    private boolean isCouldDraw;

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

    public boolean isCouldDraw() {
        return isCouldDraw;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");
        if (handlerThread == null) {
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        onSurfaceCreated(holder);
        isCouldDraw = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");

        onSurfaceChanged(holder, format, width, height);
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

        onSurfaceDestroyed(holder);
    }

    public void updateSurfaceView(final boolean isClearCanvas) {
        synchronized (this) {
            if (handler == null) {
                return;
            }

            handler.post(() -> surfaceViewDraw(isClearCanvas));
        }
    }

    private void init() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void surfaceViewDraw(final boolean isClearCanvas) {
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

            if (isClearCanvas) {
                canvas.drawPaint(paint);
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
