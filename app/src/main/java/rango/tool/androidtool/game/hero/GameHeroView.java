package rango.tool.androidtool.game.hero;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class GameHeroView extends View {

    private static final float PILLAR_HEIGHT_RATIO = 1 / 3f;
    private static final float PILLAR_WIDTH_RATIO = 1 / 7f;
    private static final float INTERSTICE_RATIO = 1 / 4f;

    private static final float PERSON_BODY_HEIGHT = ScreenUtils.dp2px(16);
    private static final float PERSON_BODY_WIDTH = ScreenUtils.dp2px(12);
    private static final float PERSON_BACKUP_DISTANCE = ScreenUtils.dp2px(8);
    private static final float PERSON_LEG_HEIGHT = ScreenUtils.dp2px(5);
    private static final float PERSON_EYE_RIGHT_MARGIN = ScreenUtils.dp2px(3);
    private static final float PERSON_EYE_TOP_MARGIN = ScreenUtils.dp2px(4);
    private static final float PERSON_EYE_SIZE = ScreenUtils.dp2px(3);

    private static final float BRIDGE_WIDTH = ScreenUtils.dp2px(4);
    private static final float BRIDGE_BACKUP_DISTANCE = ScreenUtils.dp2px(2);
    private static final float BRIDGE_GROWN_SPEED = ScreenUtils.dp2px(4);
    private static final long BRIDGE_ROTATE_DURATION = 300;

    private Bitmap bgBitmap;
    private int screenWidth;
    private int screenHeight;
    private int bitmapWidth;
    private int bitmapHeight;

    private Matrix bgMatrix;
    private float xScale;
    private float yScale;

    private Paint paint;

    private float pillarTop;
    private float pillarBottom;
    private float pillarWidth;
    private float pillarHeight;

    private float interstice;

    private float currentPillarRightPosition;

    private float currentBridgeLength = 0;
    private float bridgeMaxLength;
    private int currentBridgeRotateAngle = 0;

    private boolean isTouching = false;
    private boolean isRotating = false;

    private ValueAnimator bridgeRotateAnimator;

    public GameHeroView(Context context) {
        super(context);

        screenWidth = ScreenUtils.getScreenWidthPx();
        screenHeight = ScreenUtils.getScreenHeightPx();
        pillarHeight = screenHeight * PILLAR_HEIGHT_RATIO;
        pillarWidth = screenWidth * PILLAR_WIDTH_RATIO;
        pillarTop = screenHeight - pillarHeight;
        pillarBottom = screenHeight;

        bridgeMaxLength = screenHeight - pillarHeight;

        interstice = screenWidth * INTERSTICE_RATIO;

        currentPillarRightPosition = pillarWidth;

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        bitmapWidth = bgBitmap.getWidth();
        bitmapHeight = bgBitmap.getHeight();

        xScale = screenWidth / (float) bitmapWidth;
        yScale = screenHeight / (float) bitmapHeight;
        bgMatrix = new Matrix();
        bgMatrix.setScale(xScale, yScale);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                isRotating = false;
                startDrawBridge();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                isRotating = true;
                startRotateBridge();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawBitmap(bgBitmap, bgMatrix, paint);

        paint.setStyle(Paint.Style.FILL);
        float contentWidth = 0;
        while (contentWidth < screenWidth) {
            float l = contentWidth;
            float r = l + pillarWidth;
            canvas.drawRect(l, pillarTop, r, pillarBottom, paint);

            contentWidth = r + interstice;
        }

        drawPerson(canvas);

        drawBridge(canvas);
    }

    private void drawPerson(Canvas canvas) {

        paint.setStrokeWidth(ScreenUtils.dp2px(2));
        float l = currentPillarRightPosition - PERSON_BODY_WIDTH - PERSON_BACKUP_DISTANCE;
        float t = pillarTop - PERSON_LEG_HEIGHT - PERSON_BODY_HEIGHT;
        float r = l + PERSON_BODY_WIDTH;
        float b = t + PERSON_BODY_HEIGHT;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(l, t, r, b, paint);


        float radius = PERSON_EYE_SIZE / 2f;
        float cx = r - PERSON_EYE_RIGHT_MARGIN - radius;
        float cy = t + PERSON_EYE_TOP_MARGIN + radius;
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    private void drawBridge(Canvas canvas) {
        if (!isTouching && !isRotating && currentBridgeLength == 0) {
            return;
        }

        paint.setStrokeWidth(BRIDGE_WIDTH);
        float startX = currentPillarRightPosition - BRIDGE_BACKUP_DISTANCE - BRIDGE_WIDTH / 2f;
        float startY = pillarTop;
        if (isTouching) {
            currentBridgeLength += BRIDGE_GROWN_SPEED;
            if (currentBridgeLength > bridgeMaxLength) {
                currentBridgeLength = bridgeMaxLength;
            }
            float stopY = startY - currentBridgeLength;
            if (stopY < 0) {
                stopY = 0;
            }
            canvas.drawLine(startX, startY, startX, stopY, paint);
            grownDrawBridge();
        } else {
            double p = Math.PI / 180 * currentBridgeRotateAngle;

            float stopX = (float) (startX + currentBridgeLength * Math.sin(p));
            if (stopX > screenWidth) {
                stopX = screenWidth;
            }
            float stopY = (float) (startY - currentBridgeLength * Math.cos(p));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    private void startDrawBridge() {
        currentBridgeLength = 0;
        currentBridgeRotateAngle = 0;
        invalidate();
    }

    private void grownDrawBridge() {
        if (isTouching) {
            invalidate();
        }
    }

    private void startRotateBridge() {
        if (bridgeRotateAnimator == null) {
            bridgeRotateAnimator = ValueAnimator.ofInt(0, 90);
            bridgeRotateAnimator.setInterpolator(new AccelerateInterpolator());
            bridgeRotateAnimator.addUpdateListener(animation -> {
                currentBridgeRotateAngle = (int) animation.getAnimatedValue();
                invalidate();
            });
            bridgeRotateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    isRotating = false;
                }

                @Override public void onAnimationEnd(Animator animation) {
                    isRotating = false;
                }
            });
            bridgeRotateAnimator.setDuration(BRIDGE_ROTATE_DURATION);
        }
        bridgeRotateAnimator.start();
    }

}
