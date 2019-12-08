package rango.tool.androidtool.falling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Random;

import rango.tool.common.utils.ScreenUtils;

public class FallingPathSurfaceView extends FallingSurfaceView {

    private static final String PATH_DATA_RECTANGLE = "M23,18V6c0,-1.1 -0.9,-2 -2,-2H3c-1.1,0 -2,0.9 -2,2v12c0,1.1 0.9,2 2,2h18c1.1,0 2,-0.9 2,-2z";
    private static final String PATH_DATA_TRIANGLE = "M8,5v14l11,-7z";
    private static final String PATH_DATA_FIVE_POINTED_START = "M12,17.27L18.18,21l-1.64,-7.03L22,9.24l-7.19,-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21z";

    private static final int[] COLORS = new int[]{0xff29ffae, 0xff3983fe, 0xfffe246c, 0xffffe612};
    private static final float[] ALPHA = new float[]{0.8f, 0.6f, 0.5f};

    private static final float DISTRIBUTE_RATIO_IN_WIDTH = 1.4f;

    private static final int FALLING_ITEM_COUNT = 176;

    public static final int FALLING_SHAPE_RECTANGLE = 0;
    public static final int FALLING_SHAPE_TRIANGLE = 1;
    public static final int FALLING_SHAPE_FIVE_POINTED_STAR = 2;

    private SparseArray<Path> mShapePathSparse = new SparseArray<>();
    private int mCurrentShape;

    private Path contentPath;

    public FallingPathSurfaceView(Context context) {
        this(context, null);
    }

    public FallingPathSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingPathSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        contentPath = new Path();
        fallingItems = new ArrayList<>();

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

    @Override
    protected void createFallingItems(int increaseDistanceFactor) {
        fallingItems.clear();

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
                alpha = ALPHA[random.nextInt(ALPHA.length)];
//                color = Color.WHITE;
//            }
            fallingItems.add(new FallingPathItem(posX, posY, fallingShape, alpha, color, increaseDistanceFactor));
        }
    }

    @Override
    protected void onSurfaceViewDraw(Canvas canvas,FallingPathItem fallingItem) {

        contentPath.reset();
        contentPath.addPath(fallingItem.path);
        contentPath.transform(contentMatrix);

        canvas.drawPath(contentPath, contentPaint);
    }

}
