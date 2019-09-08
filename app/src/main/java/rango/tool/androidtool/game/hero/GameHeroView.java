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
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class GameHeroView extends View {

    private static final float PILLAR_HEIGHT_RATIO = 1 / 3f;
    private static final float PILLAR_WIDTH_MAX_RATIO = 1 / 4f;
    private static final float PILLAR_WIDTH_MIN_RATIO = 1 / 7f;
    private static final float INTERSTICE_MAX_RATIO = 1 / 3f;
    private static final float INTERSTICE_MIN_RATIO = 1 / 6f;

    private static final float FIRST_PILLAR_LEFT_RATIO = 1 / 5f;

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

    private static final float PILLAR_CENTER_RECT_WIDTH = ScreenUtils.dp2px(4);
    private static final float PILLAR_CENTER_RECT_HEIGHT = ScreenUtils.dp2px(3);

    /**
     * px per second
     */
    private static final float WALK_SPEED = ScreenUtils.dp2px(260);

    /**
     * px per second
     */
    private static final float GO_TO_NEXT_LEVEL_SPEED = ScreenUtils.dp2px(300);

    private static final long FALL_DURATION = 300;

    private static final int STATUS_INIT = 0;
    private static final int STATUS_IDLE = 1;
    private static final int STATUS_BRIDGE_GROWING = 2;
    private static final int STATUS_BRIDGE_ROTATING = 3;
    private static final int STATUS_PERSON_WALKING = 4;
    private static final int STATUS_SHOW_NEXT_LEVEL = 5;
    private static final int STATUS_FAILURE = 6;

    @IntDef({STATUS_INIT, STATUS_IDLE, STATUS_BRIDGE_GROWING,
            STATUS_BRIDGE_ROTATING, STATUS_PERSON_WALKING,
            STATUS_SHOW_NEXT_LEVEL, STATUS_FAILURE})
    private @interface StatusDef {
    }

    private @StatusDef int currentStatus;

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
    private float pillarMaxWidth;
    private float pillarMinWidth;
    private float pillarHeight;

    private float intersticeMaxDistance;
    private float intersticeMinDistance;

    private float firstPillarLeft;
    private float firstPillarRight;

    private float secondPillarLeft;
    private float secondPillarRight;

    private float thirdPillarLeft;
    private float thirdPillarRight;

    private float currentBridgeLength = 0;
    private float bridgeMaxLength;
    private int currentBridgeRotateAngle = 0;

    private float currentWalkDistance = 0;

    private float progressToNextLevel = 0f;

    private float currentFallDistance = 0f;

    private float currentPillarsMoveMaxDistance;
    private float futurePillarMoveMaxDistance;

    private final float firstPillarLeftMinDistance;

    private ValueAnimator bridgeRotateAnimator;
    private ValueAnimator walkAnimator;
    private ValueAnimator levelAnimator;
    private ValueAnimator fallAnimator;

    private OnStatusListener statusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        statusListener = listener;
    }

    public void retry() {
        currentStatus = STATUS_INIT;
        invalidate();
    }

    public GameHeroView(Context context) {
        this(context, null);
    }

    public GameHeroView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        screenWidth = ScreenUtils.getScreenWidthPx();
        screenHeight = ScreenUtils.getScreenHeightPx();
        pillarHeight = screenHeight * PILLAR_HEIGHT_RATIO;
        pillarMaxWidth = screenWidth * PILLAR_WIDTH_MAX_RATIO;
        pillarMinWidth = screenWidth * PILLAR_WIDTH_MIN_RATIO;
        pillarTop = screenHeight - pillarHeight;
        pillarBottom = screenHeight;

        firstPillarLeftMinDistance = screenWidth * FIRST_PILLAR_LEFT_RATIO;

        bridgeMaxLength = screenHeight - pillarHeight;

        intersticeMaxDistance = screenWidth * INTERSTICE_MAX_RATIO;
        intersticeMinDistance = screenWidth * INTERSTICE_MIN_RATIO;

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        bitmapWidth = bgBitmap.getWidth();
        bitmapHeight = bgBitmap.getHeight();

        xScale = screenWidth / (float) bitmapWidth;
        yScale = screenHeight / (float) bitmapHeight;
        bgMatrix = new Matrix();
        bgMatrix.setScale(xScale, yScale);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        currentStatus = STATUS_INIT;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_SHOW_NEXT_LEVEL) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDrawBridge();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startRotateBridge();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawBg(canvas);

        drawPillar(canvas);

        drawPerson(canvas);

        drawBridge(canvas);

        if (currentStatus == STATUS_INIT) {
            currentStatus = STATUS_IDLE;
        }
    }

    private void drawBg(Canvas canvas) {
        canvas.drawBitmap(bgBitmap, bgMatrix, paint);
    }

    private void drawPillar(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);

        if (currentStatus == STATUS_INIT) {
            float l = 0;
            int i = 0;
            while (i < 2) {

                float r = l + getPillarWidth();
                if (r > screenWidth) {
                    l -= (r - screenWidth);
                }

                if (i == 0) {
                    firstPillarLeft = l;
                    firstPillarRight = r;
                } else {
                    secondPillarLeft = l;
                    secondPillarRight = r;
                }
                paint.setColor(Color.BLACK);
                canvas.drawRect(l, pillarTop, r, pillarBottom, paint);

                float cL = l + (r - l - PILLAR_CENTER_RECT_WIDTH) / 2f;
                float cT = pillarTop;
                float cR = cL + PILLAR_CENTER_RECT_WIDTH;
                float cB = cT + PILLAR_CENTER_RECT_HEIGHT;
                paint.setColor(Color.RED);
                canvas.drawRect(cL, cT, cR, cB, paint);

                l = r + getIntersticeDistance();
                i++;
            }
        } else if (currentStatus == STATUS_IDLE
                || currentStatus == STATUS_BRIDGE_GROWING
                || currentStatus == STATUS_BRIDGE_ROTATING
                || currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_FAILURE) {

            paint.setColor(Color.BLACK);
            canvas.drawRect(firstPillarLeft, pillarTop, firstPillarRight, pillarBottom, paint);
            canvas.drawRect(secondPillarLeft, pillarTop, secondPillarRight, pillarBottom, paint);

            paint.setColor(Color.RED);
            float cL = firstPillarLeft + (firstPillarRight - firstPillarLeft - PILLAR_CENTER_RECT_WIDTH) / 2f;
            float cT = pillarTop;
            float cR = cL + PILLAR_CENTER_RECT_WIDTH;
            float cB = cT + PILLAR_CENTER_RECT_HEIGHT;
            canvas.drawRect(cL, cT, cR, cB, paint);

            float ncL = secondPillarLeft + (secondPillarRight - secondPillarLeft - PILLAR_CENTER_RECT_WIDTH) / 2f;
            float ncT = pillarTop;
            float ncR = ncL + PILLAR_CENTER_RECT_WIDTH;
            float ncB = ncT + PILLAR_CENTER_RECT_HEIGHT;
            canvas.drawRect(ncL, ncT, ncR, ncB, paint);

        } else if (currentStatus == STATUS_SHOW_NEXT_LEVEL) {

            float currentPillarsMoveDistance = getCurrentPillarsMoveDistance();
            float cR = firstPillarRight - currentPillarsMoveDistance;
            if (cR > 0) {
                float cPillarW = firstPillarRight - firstPillarLeft;
                float cL = cR - cPillarW;
                if (cL < 0) {
                    cL = 0;
                }
                paint.setColor(Color.BLACK);
                canvas.drawRect(cL, pillarTop, cR, pillarBottom, paint);

                float ccR = cR - (cPillarW - PILLAR_CENTER_RECT_WIDTH) / 2f;
                if (ccR > 0) {
                    float ccL = ccR - PILLAR_CENTER_RECT_WIDTH;
                    if (ccL < 0) {
                        ccL = 0;
                    }
                    paint.setColor(Color.RED);
                    canvas.drawRect(ccL, pillarTop, ccR, pillarTop + PILLAR_CENTER_RECT_HEIGHT, paint);
                }
            }

            float nL = secondPillarLeft - currentPillarsMoveDistance;
            float nR = secondPillarRight - currentPillarsMoveDistance;
            drawCompletePillar(nL, pillarTop, nR, pillarBottom, canvas);

            float nextLevelPillarMoveDistance = getNextLevelPillarMoveDistance();
            float nnL = thirdPillarLeft - nextLevelPillarMoveDistance;
            float nnR = thirdPillarRight - nextLevelPillarMoveDistance;
            drawCompletePillar(nnL, pillarTop, nnR, pillarBottom, canvas);
        }

    }

    private void drawCompletePillar(float l, float t, float r, float b, Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(l, t, r, b, paint);

        float cL = l + (r - l - PILLAR_CENTER_RECT_WIDTH) / 2f;
        float cR = cL + PILLAR_CENTER_RECT_WIDTH;
        float cB = t + PILLAR_CENTER_RECT_HEIGHT;
        paint.setColor(Color.RED);
        canvas.drawRect(cL, t, cR, cB, paint);
    }

    private float getPillarWidth() {
        return (float) (Math.random() * (pillarMaxWidth - pillarMinWidth) + pillarMinWidth);
    }

    private float getIntersticeDistance() {
        return (float) (Math.random() * (intersticeMaxDistance - intersticeMinDistance) + intersticeMinDistance);
    }

    private void drawPerson(Canvas canvas) {

        paint.setColor(Color.BLACK);
        float l = firstPillarRight - PERSON_BODY_WIDTH - PERSON_BACKUP_DISTANCE;

        if (currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_FAILURE) {
            l += currentWalkDistance;
        } else if (currentStatus == STATUS_SHOW_NEXT_LEVEL) {
            l = l + currentWalkDistance - getCurrentPillarsMoveDistance();
        }

        float t = pillarTop - PERSON_LEG_HEIGHT - PERSON_BODY_HEIGHT;
        if (currentStatus == STATUS_FAILURE) {
            t += currentFallDistance;
        }
        float r = l + PERSON_BODY_WIDTH;
        float b = t + PERSON_BODY_HEIGHT;

        float radius = PERSON_EYE_SIZE / 2f;
        float cx = r - PERSON_EYE_RIGHT_MARGIN - radius;
        float cy = t + PERSON_EYE_TOP_MARGIN + radius;

        paint.setStrokeWidth(ScreenUtils.dp2px(2));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(l, t, r, b, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    private void drawBridge(Canvas canvas) {
        if (currentStatus == STATUS_IDLE) {
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(BRIDGE_WIDTH);
        float startX = firstPillarRight - BRIDGE_BACKUP_DISTANCE - BRIDGE_WIDTH / 2f;
        float startY = pillarTop;
        if (currentStatus == STATUS_BRIDGE_GROWING) {
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
        } else if (currentStatus == STATUS_BRIDGE_ROTATING) {
            double p = Math.PI / 180 * currentBridgeRotateAngle;

            float stopX = (float) (startX + currentBridgeLength * Math.sin(p));
            if (stopX > screenWidth) {
                stopX = screenWidth;
            }
            float stopY = (float) (startY - currentBridgeLength * Math.cos(p));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        } else if (currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_FAILURE) {
            float stopX = startX + currentBridgeLength;
            if (stopX > screenWidth) {
                stopX = screenWidth;
            }
            canvas.drawLine(startX, startY, stopX, startY, paint);
        } else if (currentStatus == STATUS_SHOW_NEXT_LEVEL) {
            startX = startX - getCurrentPillarsMoveDistance();
            float stopX = startX + currentBridgeLength;
            if (startX < 0) {
                startX = 0;
            }
            canvas.drawLine(startX, startY, stopX, startY, paint);
        }
    }

    private void startDrawBridge() {
        currentStatus = STATUS_BRIDGE_GROWING;
        currentBridgeLength = 0;
        currentBridgeRotateAngle = 0;
        invalidate();
    }

    private void grownDrawBridge() {
        if (currentStatus == STATUS_BRIDGE_GROWING) {
            invalidate();
        }
    }

    private void startRotateBridge() {
        currentStatus = STATUS_BRIDGE_ROTATING;

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
                    post(new Runnable() {
                        @Override public void run() {
                            startWalk();
                        }
                    });
                }

                @Override public void onAnimationEnd(Animator animation) {
                    post(new Runnable() {
                        @Override public void run() {
                            startWalk();
                        }
                    });

                }
            });
            bridgeRotateAnimator.setDuration(BRIDGE_ROTATE_DURATION);
        }
        bridgeRotateAnimator.start();
    }

    private void startWalk() {
        currentStatus = STATUS_PERSON_WALKING;

        if (walkAnimator != null) {
            walkAnimator.cancel();
            walkAnimator = null;
        }

        boolean isPass;
        float walkDistance;
        float correctMaxDistance = secondPillarRight - firstPillarRight + BRIDGE_BACKUP_DISTANCE;
        float correctMinDistance = secondPillarLeft - firstPillarRight + BRIDGE_BACKUP_DISTANCE;
        if (currentBridgeLength <= correctMaxDistance && currentBridgeLength >= correctMinDistance) {
            isPass = true;
            walkDistance = correctMaxDistance - BRIDGE_BACKUP_DISTANCE;
        } else {
            isPass = false;
            walkDistance = currentBridgeLength + (PERSON_BACKUP_DISTANCE - BRIDGE_BACKUP_DISTANCE) + PERSON_BODY_WIDTH;
        }
        walkAnimator = ValueAnimator.ofFloat(0, walkDistance);
        walkAnimator.setInterpolator(new LinearInterpolator());
        long duration = (long) (walkDistance / WALK_SPEED * 1000);
        walkAnimator.setDuration(duration);
        walkAnimator.addUpdateListener(animation -> {
            currentWalkDistance = (float) animation.getAnimatedValue();
            invalidate();
        });
        walkAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                post(new Runnable() {
                    @Override public void run() {
                        walkResult(isPass);
                    }
                });

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                post(new Runnable() {
                    @Override public void run() {
                        walkResult(isPass);
                    }
                });
            }
        });
        walkAnimator.start();
    }

    private void walkResult(boolean isPass) {
        if (isPass) {
            startDrawNextLevel();
        } else {
            startFall();
        }
    }

    private void startDrawNextLevel() {
        thirdPillarLeft = secondPillarRight + getIntersticeDistance();
        thirdPillarRight = thirdPillarLeft + getPillarWidth();

        currentPillarsMoveMaxDistance = secondPillarLeft;
        float maxD = secondPillarRight - firstPillarLeftMinDistance;
        if (currentPillarsMoveMaxDistance > maxD) {
            currentPillarsMoveMaxDistance = maxD;
        }

        if (thirdPillarRight - currentPillarsMoveMaxDistance > screenWidth) {
            float d = thirdPillarRight - currentPillarsMoveMaxDistance - screenWidth;
            thirdPillarLeft -= d;
            thirdPillarRight -= d;
        }

        if (thirdPillarLeft < screenWidth) {
            float dis = screenWidth - thirdPillarLeft;
            futurePillarMoveMaxDistance = currentPillarsMoveMaxDistance + dis;
            thirdPillarLeft += dis;
            thirdPillarRight += dis;

        } else {
            futurePillarMoveMaxDistance = currentPillarsMoveMaxDistance;
        }

        if (levelAnimator != null) {
            levelAnimator.cancel();
            levelAnimator = null;
        }

        levelAnimator = ValueAnimator.ofFloat(0f, 1f);
        levelAnimator.addUpdateListener(animation -> {
            progressToNextLevel = (float) animation.getAnimatedValue();
            invalidate();
        });

        levelAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                currentStatus = STATUS_SHOW_NEXT_LEVEL;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                post(new Runnable() {
                    @Override public void run() {
                        showNextLevelComplete();
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                post(new Runnable() {
                    @Override public void run() {
                        showNextLevelComplete();
                    }
                });
            }
        });

        long duration = (long) (currentPillarsMoveMaxDistance / GO_TO_NEXT_LEVEL_SPEED * 1000);
        levelAnimator.setDuration(duration);
        levelAnimator.setInterpolator(new LinearInterpolator());

        levelAnimator.start();
    }

    private float getCurrentPillarsMoveDistance() {
        return progressToNextLevel * currentPillarsMoveMaxDistance;
    }

    private float getNextLevelPillarMoveDistance() {
        return progressToNextLevel * futurePillarMoveMaxDistance;
    }

    private void showNextLevelComplete() {
        currentStatus = STATUS_IDLE;
        firstPillarLeft = secondPillarLeft - currentPillarsMoveMaxDistance;
        firstPillarRight = secondPillarRight - currentPillarsMoveMaxDistance;

        secondPillarLeft = thirdPillarLeft - futurePillarMoveMaxDistance;
        secondPillarRight = thirdPillarRight - futurePillarMoveMaxDistance;
    }

    private void startFall() {
        currentStatus = STATUS_FAILURE;
        currentFallDistance = 0f;
        if (fallAnimator == null) {
            fallAnimator = ValueAnimator.ofFloat(0, pillarHeight + PERSON_BODY_HEIGHT + PERSON_LEG_HEIGHT);
            fallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentFallDistance = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            fallAnimator.setDuration(FALL_DURATION);
            fallAnimator.setInterpolator(new AccelerateInterpolator());
            fallAnimator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationCancel(Animator animation) {
                    post(new Runnable() {
                        @Override public void run() {
                            onFallEnd();
                        }
                    });
                }

                @Override public void onAnimationEnd(Animator animation) {
                    post(new Runnable() {
                        @Override public void run() {
                            onFallEnd();
                        }
                    });
                }
            });
        }
        fallAnimator.start();
    }

    private void onFallEnd() {
        if (statusListener != null) {
            statusListener.onFailure();
        }
    }

    public interface OnStatusListener {
        void onFailure();
    }

    private class Pillar {
        private float left;
        private float top;
        private float right;
        private float bottom;
    }

    private class Bridge {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
    }

}
