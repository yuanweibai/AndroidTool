package rango.tool.androidtool.falling;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rango.tool.common.utils.ScreenUtils;

public class FallingSurfaceView extends HandlerSurfaceView {

    private static final String PATH_DATA_RECTANGLE = "M23,18V6c0,-1.1 -0.9,-2 -2,-2H3c-1.1,0 -2,0.9 -2,2v12c0,1.1 0.9,2 2,2h18c1.1,0 2,-0.9 2,-2z";
    private static final String PATH_DATA_TRIANGLE = "M8,5v14l11,-7z";
    private static final String PATH_DATA_FIVE_POINTED_START = "M12,17.27L18.18,21l-1.64,-7.03L22,9.24l-7.19,-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21z";

    private static final int[] COLORS = new int[]{0xff29ffae, 0xff3983fe, 0xfffe246c, 0xffffe612};
    private static final float[] ALPHA = new float[]{0.8f, 0.6f, 0.5f};

    private static final float DISTRIBUTE_RATIO_IN_WIDTH = 1.4f;
    private static final float RATION_REAL_UPDATE_INTERVAL = 0.75f;

    private static final int FALLING_ITEM_COUNT = 36;
    private static final float DIRECTION_ANGLE_RANGE = 0.1f;

    private static final long INTERVAL_UPDATE_DRAW = 16;

    public static final int FALLING_SHAPE_RECTANGLE = 0;
    public static final int FALLING_SHAPE_TRIANGLE = 1;
    public static final int FALLING_SHAPE_FIVE_POINTED_STAR = 2;

    private class FallingItem {
        float posX;
        float posY;

        float increaseDistance;
        float scaleRatio;
        float rotateAngle;
        float directionAngle;

        float increaseAngle;

        float alpha;
        int color;
        Path path;

        FallingItem(float posX, float posY, Path path, float alpha, int color, float increaseDistanceFactor) {
            this.posX = posX;
            this.posY = posY;
            this.alpha = alpha;
            this.color = color;
            this.path = path;

            Random random = new Random();
            this.rotateAngle = random.nextInt(360);
            this.increaseAngle = random.nextFloat() * 0.5f + 0.5f;
            this.scaleRatio = random.nextFloat() * 0.7f + 0.3f;

            this.increaseDistance = ((random.nextFloat() + 1) * increaseDistanceFactor) * getResources().getDisplayMetrics().density;
            this.directionAngle = (float) (random.nextFloat() * DIRECTION_ANGLE_RANGE + (Math.PI - DIRECTION_ANGLE_RANGE) / 2f);
        }

        void updatePosition(float intervalCoefficient) {
            posX += intervalCoefficient * increaseDistance * Math.cos(directionAngle);
            posY += intervalCoefficient * increaseDistance * Math.sin(directionAngle);

            Random random = new Random();

            directionAngle += (random.nextFloat() - 0.5f) * 0.02f;
            rotateAngle += increaseAngle + random.nextFloat() * 0.1f;

            if (posY > getHeight()) {
                if (getWidth() > 0) {
                    posX = random.nextInt(getWidth()) * DISTRIBUTE_RATIO_IN_WIDTH - (DISTRIBUTE_RATIO_IN_WIDTH / 2f - 0.5f) * getWidth();
                } else {
                    posX = 0;
                }

                posY = 0;
            }
        }
    }

    private Runnable mUpdateFallingItemsRunnable = new Runnable() {

        @Override
        public void run() {

            final long intervalUpdate = System.currentTimeMillis() - mLastUpdateTime;
            final float coefficient = (float) ((double) intervalUpdate / INTERVAL_UPDATE_DRAW);

            for (FallingItem fallingItem : mFallingItems) {
                fallingItem.updatePosition(coefficient);
            }

            mLogLastCallUpdateTime = System.currentTimeMillis();
            updateSurfaceView(true);

            mLastUpdateTime = System.currentTimeMillis();

            synchronized (FallingSurfaceView.this) {
                if (mHandler != null) {
                    mHandler.postDelayed(this, (long) (INTERVAL_UPDATE_DRAW * (1 - RATION_REAL_UPDATE_INTERVAL)
                            + intervalUpdate * RATION_REAL_UPDATE_INTERVAL));
                }
            }
        }
    };

    private Runnable mRemoveFallingItemsUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (FallingSurfaceView.this) {
                if (mHandler == null) {
                    return;
                }
                mHandler.removeCallbacks(mUpdateFallingItemsRunnable);
            }

            mFallingItems.clear();

            mLogLastCallUpdateTime = System.currentTimeMillis();
            updateSurfaceView(true);
        }
    };

    private Runnable mOnSurfaceCreateRunnable;

    private SparseArray<Path> mShapePathSparse = new SparseArray<>();
    private Paint mPaint = new Paint();
    private Matrix mMatrix = new Matrix();
    private Path mPath = new Path();

    private List<FallingItem> mFallingItems = new ArrayList<>();
    private long mLastUpdateTime;

    private long mLogLastDrawEndTime;
    private long mLogLastCallUpdateTime;

    private int mShapeAlpha = 255;
    private ValueAnimator mValueAnimator;
    private int mCurrentShape;

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

    public void setShape(int shape) {
        mCurrentShape = shape;
    }

    public void startFallingAnim() {
        startFallingAnim(false, 0, 3f);
    }

    public void startFallingAnim(long mill, float itemIncreaseDistanceFactor, float startAlpha) {
        startFallingAnim(true, mill, itemIncreaseDistanceFactor);

        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }

        mValueAnimator = ValueAnimator.ofFloat(startAlpha, 1f, 1f, 0f);
        mValueAnimator.addUpdateListener(animation -> mShapeAlpha = (int) (255 * (float) animation.getAnimatedValue()));
        mValueAnimator.setDuration(mill);
        mValueAnimator.start();
    }

    private void startFallingAnim(boolean isAutoStop, long mills, float itemIncreaseDistanceFactor) {
        synchronized (this) {
            if (!isSurfaceReady()) {
                return;
            }
            mHandler.removeCallbacks(mRemoveFallingItemsUpdateRunnable);
            if (isAutoStop) {
                mHandler.postDelayed(mRemoveFallingItemsUpdateRunnable, mills);
            }

            mLastUpdateTime = System.currentTimeMillis();

            mHandler.removeCallbacks(mUpdateFallingItemsRunnable);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    createFallingItems(itemIncreaseDistanceFactor);
                }
            });
            mHandler.post(mUpdateFallingItemsRunnable);
        }
    }

    public void stopFallingAnim() {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.removeCallbacks(mRemoveFallingItemsUpdateRunnable);
                mHandler.post(mRemoveFallingItemsUpdateRunnable);
            }
        }
    }

    public boolean isSurfaceReady() {
        return mHandler != null;
    }

    public void setOnSurfaceCreateRunnable(Runnable onSurfaceCreateRunnable) {
        this.mOnSurfaceCreateRunnable = onSurfaceCreateRunnable;
    }

    public void removeOnSurfaceCreateRunnable() {
        mOnSurfaceCreateRunnable = null;
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder) {
        if (mOnSurfaceCreateRunnable != null) {
            mOnSurfaceCreateRunnable.run();
        }
    }

    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void onSurfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onSurfaceViewDraw(Canvas canvas) {
        final long drawStartTime = System.currentTimeMillis();

        for (FallingItem fallingItem : mFallingItems) {

            if (fallingItem.posX < 0 || fallingItem.posX > getWidth()) {
                continue;
            }

            mMatrix.setRotate(fallingItem.rotateAngle);
            mMatrix.postScale(fallingItem.scaleRatio, fallingItem.scaleRatio);
            mMatrix.postTranslate(fallingItem.posX, fallingItem.posY);

            mPath.reset();
            mPath.addPath(fallingItem.path);
            mPath.transform(mMatrix);

            mPaint.setColor(fallingItem.color);
            mPaint.setAlpha((int) (mShapeAlpha * fallingItem.alpha));

            canvas.drawPath(mPath, mPaint);
        }

        final long drawEndTime = System.currentTimeMillis();

        Log.d("FALLING_DRAW_", "init background time is " + (drawStartTime - mLogLastCallUpdateTime)
                + ", draw falling items time is " + (drawEndTime - drawStartTime)
                + ", call draw interval time is " + (drawEndTime - mLogLastDrawEndTime));

        mLogLastDrawEndTime = System.currentTimeMillis();
    }

    private void init() {

        setZOrderOnTop(true);

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        SvgParser svgParser = new SvgParser();
        Matrix matrix = new Matrix();

        final float rectangleWidth = ScreenUtils.dp2px(24);
        final float rectangleHeight = ScreenUtils.dp2px(24);
        matrix.setScale(rectangleWidth / 24f, rectangleHeight / 24f);

        SvgParser.PathDataInfo rectanglePathDataInfo = svgParser.parserPathInfo(PATH_DATA_RECTANGLE);
        rectanglePathDataInfo.updatePath();
        rectanglePathDataInfo.path.transform(matrix);
        mShapePathSparse.put(FALLING_SHAPE_RECTANGLE, rectanglePathDataInfo.path);

        final float triangleWidth = ScreenUtils.dp2px(48);
        final float triangleHeight = ScreenUtils.dp2px(48);
        matrix.setScale(triangleWidth / 24f, triangleHeight / 24f);

        SvgParser.PathDataInfo trianglePathDataInfo = svgParser.parserPathInfo(PATH_DATA_TRIANGLE);
        trianglePathDataInfo.updatePath();
        trianglePathDataInfo.path.transform(matrix);
        mShapePathSparse.put(FALLING_SHAPE_TRIANGLE, trianglePathDataInfo.path);

        final float starWidth = ScreenUtils.dp2px(50);
        final float starHeight = ScreenUtils.dp2px(50);
        matrix.setScale(starWidth / 24f, starHeight / 24f);

        SvgParser.PathDataInfo fiveStartPathDataInfo = svgParser.parserPathInfo(PATH_DATA_FIVE_POINTED_START);
        fiveStartPathDataInfo.updatePath();
        fiveStartPathDataInfo.path.transform(matrix);
        mShapePathSparse.put(FALLING_SHAPE_FIVE_POINTED_STAR, fiveStartPathDataInfo.path);
    }

    private void createFallingItems(float increaseDistanceFactor) {
        mFallingItems.clear();

        Random random = new Random();
        if (mCurrentShape < 0 || mCurrentShape >= mShapePathSparse.size()) {
            mCurrentShape = random.nextInt(mShapePathSparse.size());
        }
        Path fallingShape = mShapePathSparse.valueAt(mCurrentShape);

        for (int i = 0; i < FALLING_ITEM_COUNT; i++) {
            final float posX = getWidth() <= 0 ? 0 : random.nextInt(getWidth()) * DISTRIBUTE_RATIO_IN_WIDTH - (DISTRIBUTE_RATIO_IN_WIDTH / 2f - 0.5f) * getWidth();
            final float posY = getHeight() <= 0 ? 0 : random.nextInt(getHeight()) - getHeight();
            float alpha;
            int color;
//            if (isFactorEquals(increaseDistanceFactor, 3f)) {
            alpha = 1f;
            color = COLORS[random.nextInt(COLORS.length)];
//            } else {
//                alpha = ALPHA[random.nextInt(ALPHA.length)];
//                color = Color.WHITE;
//            }
            mFallingItems.add(new FallingItem(posX, posY, fallingShape, alpha, color, increaseDistanceFactor));
        }
    }

    private boolean isFactorEquals(float f1, float f2) {
        return Math.abs(f1 - f2) < 0.0005f;
    }

    public void onDestroy() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }

        stopFallingAnim();
    }
}
