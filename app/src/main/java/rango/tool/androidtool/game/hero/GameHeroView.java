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

    private static final float PERSON_BODY_HEIGHT = ScreenUtils.dp2px(16);
    private static final float PERSON_BODY_WIDTH = ScreenUtils.dp2px(12);
    private static final float PERSON_BACKUP_DISTANCE = ScreenUtils.dp2px(8);
    private static final float PERSON_LEG_HEIGHT = ScreenUtils.dp2px(5);
    private static final float PERSON_LEG_WIDTH = ScreenUtils.dp2px(2);
    private static final float PERSON_LEG_OFFSET = ScreenUtils.dp2px(2);
    private static final float PERSON_LEG_WALK_OFFSET = ScreenUtils.dp2px(3);
    private static final float PERSON_EYE_RIGHT_MARGIN = ScreenUtils.dp2px(3);
    private static final float PERSON_EYE_TOP_MARGIN = ScreenUtils.dp2px(4);
    private static final float PERSON_EYE_SIZE = ScreenUtils.dp2px(3);
    private static final float PERSON_SCALE_OFFSET = ScreenUtils.dp2px(0.8f);


    private static final float BRIDGE_WIDTH = ScreenUtils.dp2px(3);
    private static final float BRIDGE_BACKUP_DISTANCE = ScreenUtils.dp2px(2);

    private static float PERFECT_RECT_WIDTH;
    private static float PERFECT_RECT_HEIGHT;

    private static final int STATUS_INIT = 0;
    private static final int STATUS_IDLE = 1;
    private static final int STATUS_BRIDGE_GROWING = 2;
    private static final int STATUS_BRIDGE_ROTATING = 3;
    private static final int STATUS_PERSON_KICK = 7;
    private static final int STATUS_PERSON_WALKING = 4;
    private static final int STATUS_SHOW_NEXT_LEVEL = 5;
    private static final int STATUS_FAILURE = 6;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private float FIRST_PILLAR_LEFT_MIN_DISTANCE;

    private float PILLAR_TOP;
    private float PILLAR_BOTTOM;
    private float PILLAR_HEIGHT;

    @IntDef({STATUS_INIT, STATUS_IDLE, STATUS_BRIDGE_GROWING,
            STATUS_BRIDGE_ROTATING, STATUS_PERSON_KICK, STATUS_PERSON_WALKING,
            STATUS_SHOW_NEXT_LEVEL, STATUS_FAILURE})
    private @interface StatusDef {
    }

    private @StatusDef int currentStatus;

    private Bitmap bgBitmap;
    private Bitmap contentBitmap;

    private int contentBitmapHeight;

    private float currentContentTranslationX = 0;

    private Matrix bgMatrix;
    private float xScale;
    private float yScale;

    private float contentXScale;

    private Paint paint;

    private float firstPillarLeft;
    private float firstPillarRight;

    private float secondPillarLeft;
    private float secondPillarRight;

    private float thirdPillarLeft;
    private float thirdPillarRight;

    private float lastBridgeLength = 0;
    private float currentBridgeLength = 0;
    private float BRIDGE_MAX_LENGTH;
    private int currentBridgeRotateAngle = 0;

    private float lastWalkDistanceForLeg;
    private float currentWalkDistance = 0;
    private float lastWalkDistance = 0;

    private float progressToNextLevel = 0f;

    private float currentFallDistance = 0f;

    private float currentPillarsMoveMaxDistance;
    private float futurePillarMoveMaxDistance;

    private int currentScore = 0;

    private boolean isFlag = false;

    private ValueAnimator bridgeRotateAnimator;
    private ValueAnimator walkAnimator;
    private ValueAnimator levelAnimator;
    private ValueAnimator fallAnimator;

    private OnStatusListener statusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        statusListener = listener;
    }

    public void retry() {
        reset();
        invalidate();
    }

    private void reset() {
        currentStatus = STATUS_INIT;
        currentScore = 0;
        currentContentTranslationX = 0;
    }

    public GameHeroView(Context context) {
        this(context, null);
    }

    public GameHeroView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initFinalSize();

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        contentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);

        int bitmapWidth = bgBitmap.getWidth();
        int bitmapHeight = bgBitmap.getHeight();

        int contentBitmapWidth = contentBitmap.getWidth();
        contentBitmapHeight = contentBitmap.getHeight();

        xScale = SCREEN_WIDTH / (float) bitmapWidth;
        yScale = SCREEN_HEIGHT / (float) bitmapHeight;

        contentXScale = SCREEN_WIDTH / (float) contentBitmapWidth;

        bgMatrix = new Matrix();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        currentStatus = STATUS_INIT;
    }

    private void initFinalSize() {
        SCREEN_WIDTH = ScreenUtils.getScreenWidthPx();
        SCREEN_HEIGHT = ScreenUtils.getScreenHeightPx();
        PERFECT_RECT_WIDTH = GameHeroConstants.PERFECT_RECT_WIDTH_RATIO * SCREEN_WIDTH;
        PERFECT_RECT_HEIGHT = PERFECT_RECT_WIDTH * GameHeroConstants.PERFECT_RECT_HEIGHT_WIDTH_RATIO;

        PILLAR_HEIGHT = SCREEN_HEIGHT * GameHeroConstants.PILLAR_HEIGHT_RATIO;
        PILLAR_TOP = SCREEN_HEIGHT - PILLAR_HEIGHT;
        PILLAR_BOTTOM = SCREEN_HEIGHT;

        FIRST_PILLAR_LEFT_MIN_DISTANCE = SCREEN_WIDTH * GameHeroConstants.FIRST_PILLAR_LEFT_MIN_RATIO;

        BRIDGE_MAX_LENGTH = SCREEN_HEIGHT - PILLAR_HEIGHT;
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
                kickBridge();
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

        bgMatrix.reset();
        bgMatrix.setScale(xScale, yScale);
        canvas.drawBitmap(bgBitmap, bgMatrix, paint);


        if (currentStatus == STATUS_PERSON_WALKING) {
            currentContentTranslationX += (currentWalkDistance - lastWalkDistance) / 5;
            if (currentContentTranslationX >= SCREEN_WIDTH) {
                currentContentTranslationX -= SCREEN_WIDTH;
            }

        }
        float dy = (SCREEN_HEIGHT - contentBitmapHeight * contentXScale);
        bgMatrix.reset();
        bgMatrix.setScale(contentXScale, contentXScale);
        bgMatrix.postTranslate(-currentContentTranslationX, dy);
        canvas.drawBitmap(contentBitmap, bgMatrix, paint);

        bgMatrix.reset();
        bgMatrix.setScale(contentXScale, contentXScale);
        bgMatrix.postTranslate(SCREEN_WIDTH - currentContentTranslationX, dy);
        canvas.drawBitmap(contentBitmap, bgMatrix, paint);
    }

    private void drawPillar(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);

        if (currentStatus == STATUS_INIT) {
            float l = 0;
            int i = 0;
            while (i < 2) {

                float r = l + getPillarWidth();

                if (i == 0) {
                    if (r > SCREEN_WIDTH / 2f) {
                        r = SCREEN_WIDTH / 2f;
                    }
                } else {
                    if (r > SCREEN_WIDTH) {
                        r = SCREEN_WIDTH;
                    }
                }

                if (i == 0) {
                    firstPillarLeft = l;
                    firstPillarRight = r;
                } else {
                    secondPillarLeft = l;
                    secondPillarRight = r;
                }
                paint.setColor(Color.BLACK);
                canvas.drawRect(l, PILLAR_TOP, r, PILLAR_BOTTOM, paint);

                float cL = l + (r - l - PERFECT_RECT_WIDTH) / 2f;
                float cT = PILLAR_TOP;
                float cR = cL + PERFECT_RECT_WIDTH;
                float cB = cT + PERFECT_RECT_HEIGHT;
                paint.setColor(Color.RED);
                canvas.drawRect(cL, cT, cR, cB, paint);

                l = r + getIntersticeDistance();
                if (l >= (SCREEN_WIDTH - 2 * PERFECT_RECT_WIDTH)) {
                    l = SCREEN_WIDTH - 2 * PERFECT_RECT_WIDTH;
                }
                i++;
            }
        } else if (currentStatus == STATUS_IDLE
                || currentStatus == STATUS_BRIDGE_GROWING
                || currentStatus == STATUS_BRIDGE_ROTATING
                || currentStatus == STATUS_PERSON_KICK
                || currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_FAILURE) {

            paint.setColor(Color.BLACK);
            canvas.drawRect(firstPillarLeft, PILLAR_TOP, firstPillarRight, PILLAR_BOTTOM, paint);
            canvas.drawRect(secondPillarLeft, PILLAR_TOP, secondPillarRight, PILLAR_BOTTOM, paint);

            paint.setColor(Color.RED);
            float cL = firstPillarLeft + (firstPillarRight - firstPillarLeft - PERFECT_RECT_WIDTH) / 2f;
            float cT = PILLAR_TOP;
            float cR = cL + PERFECT_RECT_WIDTH;
            float cB = cT + PERFECT_RECT_HEIGHT;
            canvas.drawRect(cL, cT, cR, cB, paint);

            float ncL = secondPillarLeft + (secondPillarRight - secondPillarLeft - PERFECT_RECT_WIDTH) / 2f;
            float ncT = PILLAR_TOP;
            float ncR = ncL + PERFECT_RECT_WIDTH;
            float ncB = ncT + PERFECT_RECT_HEIGHT;
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
                canvas.drawRect(cL, PILLAR_TOP, cR, PILLAR_BOTTOM, paint);

                float ccR = cR - (cPillarW - PERFECT_RECT_WIDTH) / 2f;
                if (ccR > 0) {
                    float ccL = ccR - PERFECT_RECT_WIDTH;
                    if (ccL < 0) {
                        ccL = 0;
                    }
                    paint.setColor(Color.RED);
                    canvas.drawRect(ccL, PILLAR_TOP, ccR, PILLAR_TOP + PERFECT_RECT_HEIGHT, paint);
                }
            }

            float nL = secondPillarLeft - currentPillarsMoveDistance;
            float nR = secondPillarRight - currentPillarsMoveDistance;
            drawCompletePillar(nL, PILLAR_TOP, nR, PILLAR_BOTTOM, canvas);

            float nextLevelPillarMoveDistance = getNextLevelPillarMoveDistance();
            float nnL = thirdPillarLeft - nextLevelPillarMoveDistance;
            float nnR = thirdPillarRight - nextLevelPillarMoveDistance;
            drawCompletePillar(nnL, PILLAR_TOP, nnR, PILLAR_BOTTOM, canvas);
        }

    }

    private void drawCompletePillar(float l, float t, float r, float b, Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(l, t, r, b, paint);

        float cL = l + (r - l - PERFECT_RECT_WIDTH) / 2f;
        float cR = cL + PERFECT_RECT_WIDTH;
        float cB = t + PERFECT_RECT_HEIGHT;
        paint.setColor(Color.RED);
        canvas.drawRect(cL, t, cR, cB, paint);
    }

    private float getPillarWidth() {
        int hierarchy = getCurrentHierarchy();
        int[] pillars = GameHeroConstants.PILLAR_WIDTH_MULTIPLE[hierarchy];
        int multiple;
        if (pillars.length == 1) {
            multiple = pillars[0];
        } else {
            int index = (int) (Math.random() * pillars.length);
            multiple = pillars[index];
        }
        return multiple * PERFECT_RECT_WIDTH;
    }

    private float getIntersticeDistance() {
        int hierarchy = getCurrentHierarchy();
        int[] pillars = GameHeroConstants.INTERSTICE_WIDTH_MULTIPLE[hierarchy];
        int multiple;
        if (pillars.length == 1) {
            multiple = pillars[0];
        } else {
            int index = (int) (Math.random() * pillars.length);
            multiple = pillars[index];
        }
        return multiple * PERFECT_RECT_WIDTH;
    }

    private int getCurrentHierarchy() {
        for (int i = 0; i < GameHeroConstants.GAME_HIERARCHY.length; i++) {
            int score = GameHeroConstants.GAME_HIERARCHY[i];
            if (currentScore <= score) {
                return i;
            }
        }
        return 0;
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

        float t = PILLAR_TOP - PERSON_LEG_HEIGHT - PERSON_BODY_HEIGHT;
        if (currentStatus == STATUS_FAILURE) {
            t += currentFallDistance;
        }
        float r = l + PERSON_BODY_WIDTH;
        float b = t + PERSON_BODY_HEIGHT;

        if (currentStatus == STATUS_BRIDGE_GROWING) {

            if (currentBridgeLength - lastBridgeLength > 90) {
                lastBridgeLength = currentBridgeLength;
                isFlag = !isFlag;
            }

            if (!isFlag) {
                l -= PERSON_SCALE_OFFSET;
                r += PERSON_SCALE_OFFSET;
                t += PERSON_SCALE_OFFSET;
            }
        }

        float radius = PERSON_EYE_SIZE / 2f;
        float cx = r - PERSON_EYE_RIGHT_MARGIN - radius;
        float cy = t + PERSON_EYE_TOP_MARGIN + radius;

        paint.setStrokeWidth(ScreenUtils.dp2px(2));
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(l, t, r, b, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, radius, paint);

        float startX1 = l + PERSON_LEG_OFFSET;
        float stopY1 = b + PERSON_LEG_HEIGHT;
        float stopX1 = startX1;

        float startX2 = r - PERSON_LEG_OFFSET;
        float stopX2 = startX2;
        float stopY2 = b + PERSON_LEG_HEIGHT;

        if (currentStatus == STATUS_PERSON_WALKING && currentWalkDistance - lastWalkDistanceForLeg > 10) {
            lastWalkDistanceForLeg = currentWalkDistance;
            if (isFlag) {
                isFlag = false;
                stopX1 = startX1 + PERSON_LEG_WALK_OFFSET;
                stopX2 = startX2 - PERSON_LEG_WALK_OFFSET;
            } else {
                isFlag = true;
                stopX1 = startX1 - PERSON_LEG_WALK_OFFSET;
                stopX2 = startX2 + PERSON_LEG_WALK_OFFSET;
            }
        } else if (currentStatus == STATUS_PERSON_KICK) {
            stopX2 = startX2 + (PERSON_BACKUP_DISTANCE - BRIDGE_BACKUP_DISTANCE);
            stopY2 = stopY2 - 4;
        }

        paint.setStrokeWidth(PERSON_LEG_WIDTH);
        canvas.drawLine(startX1, b, stopX1, stopY1, paint);
        canvas.drawLine(startX2, b, stopX2, stopY2, paint);
    }

    private void drawBridge(Canvas canvas) {
        if (currentStatus == STATUS_IDLE) {
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(BRIDGE_WIDTH);
        float startX = firstPillarRight - BRIDGE_BACKUP_DISTANCE - BRIDGE_WIDTH / 2f;
        float startY = PILLAR_TOP;
        if (currentStatus == STATUS_BRIDGE_GROWING) {
            currentBridgeLength += GameHeroConstants.BRIDGE_GROWING_SPEED;
            if (currentBridgeLength > BRIDGE_MAX_LENGTH) {
                currentBridgeLength = BRIDGE_MAX_LENGTH;
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
            if (stopX > SCREEN_WIDTH) {
                stopX = SCREEN_WIDTH;
            }
            float stopY = (float) (startY - currentBridgeLength * Math.cos(p));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        } else if (currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_FAILURE) {
            float stopX = startX + currentBridgeLength;
            if (stopX > SCREEN_WIDTH) {
                stopX = SCREEN_WIDTH;
            }
            canvas.drawLine(startX, startY, stopX, startY, paint);
        } else if (currentStatus == STATUS_SHOW_NEXT_LEVEL) {
            startX = startX - getCurrentPillarsMoveDistance();
            float stopX = startX + currentBridgeLength;
            if (startX < 0) {
                startX = 0;
            }
            canvas.drawLine(startX, startY, stopX, startY, paint);
        } else if (currentStatus == STATUS_PERSON_KICK) {
            float stopY = startY - currentBridgeLength;
            if (stopY < 0) {
                stopY = 0;
            }
            canvas.drawLine(startX, startY, startX, stopY, paint);
        }
    }

    private void startDrawBridge() {
        currentStatus = STATUS_BRIDGE_GROWING;
        currentBridgeLength = 0;
        currentBridgeRotateAngle = 0;
        isFlag = false;
        lastBridgeLength = 0;
        invalidate();
    }

    private void grownDrawBridge() {
        if (currentStatus == STATUS_BRIDGE_GROWING) {
            invalidate();
        }
    }

    private void kickBridge() {
        currentStatus = STATUS_PERSON_KICK;
        invalidate();

        postDelayed(new Runnable() {
            @Override public void run() {
                startRotateBridge();
            }
        }, 200);
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
            bridgeRotateAnimator.setDuration(GameHeroConstants.BRIDGE_ROTATE_DURATION);
        }
        bridgeRotateAnimator.start();
    }

    private void startWalk() {
        currentStatus = STATUS_PERSON_WALKING;
        isFlag = false;
        lastWalkDistanceForLeg = 0;
        if (walkAnimator != null) {
            walkAnimator.cancel();
            walkAnimator = null;
        }

        boolean isPass;
        float walkDistance;
        float extraDis = BRIDGE_BACKUP_DISTANCE + BRIDGE_WIDTH / 2f;
        float correctMaxDistance = secondPillarRight - firstPillarRight + extraDis;
        float correctMinDistance = secondPillarLeft - firstPillarRight + extraDis;
        if (currentBridgeLength <= correctMaxDistance && currentBridgeLength >= correctMinDistance) {
            isPass = true;
            walkDistance = correctMaxDistance - extraDis;
            float secondPillarWidth = secondPillarRight - secondPillarLeft;
            float perfectMaxDistance = (secondPillarRight - secondPillarWidth / 2f + PERFECT_RECT_WIDTH / 2f) - firstPillarRight + extraDis;
            float perfectMinDistance = (secondPillarRight - secondPillarWidth / 2f - PERFECT_RECT_WIDTH / 2f) - firstPillarRight + extraDis;
            if (currentBridgeLength <= perfectMaxDistance && currentBridgeLength >= perfectMinDistance) {
                currentScore++;
                if (statusListener != null) {
                    statusListener.onPerfect(currentScore);
                }
            }
        } else {
            isPass = false;
            walkDistance = currentBridgeLength + (PERSON_BACKUP_DISTANCE - BRIDGE_BACKUP_DISTANCE) + PERSON_BODY_WIDTH;
        }
        walkAnimator = ValueAnimator.ofFloat(0, walkDistance);
        walkAnimator.setInterpolator(new LinearInterpolator());
        long duration = (long) (walkDistance / GameHeroConstants.WALK_SPEED * 1000);
        walkAnimator.setDuration(duration);
        walkAnimator.addUpdateListener(animation -> {
            lastWalkDistance = currentWalkDistance;
            currentWalkDistance = (float) animation.getAnimatedValue();
            invalidate();
        });
        walkAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                postDelayed(new Runnable() {
                    @Override public void run() {
                        walkResult(isPass);
                    }
                }, 100);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(new Runnable() {
                    @Override public void run() {
                        walkResult(isPass);
                    }
                }, 100);
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
        float maxD = secondPillarRight - FIRST_PILLAR_LEFT_MIN_DISTANCE;
        if (currentPillarsMoveMaxDistance > maxD) {
            currentPillarsMoveMaxDistance = maxD;
        }

        if (thirdPillarRight - currentPillarsMoveMaxDistance > SCREEN_WIDTH) {
            float d = thirdPillarRight - currentPillarsMoveMaxDistance - SCREEN_WIDTH;
            thirdPillarLeft -= d;
            thirdPillarRight -= d;
        }

        if (thirdPillarLeft < SCREEN_WIDTH) {
            float dis = SCREEN_WIDTH - thirdPillarLeft;
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

        long duration = (long) (currentPillarsMoveMaxDistance / GameHeroConstants.GO_TO_NEXT_LEVEL_SPEED * 1000);
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
        currentScore++;
        if (statusListener != null) {
            statusListener.onSuccess(currentScore);
        }
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
            fallAnimator = ValueAnimator.ofFloat(0, PILLAR_HEIGHT + PERSON_BODY_HEIGHT + PERSON_LEG_HEIGHT);
            fallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentFallDistance = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            fallAnimator.setDuration(GameHeroConstants.FALL_DURATION);
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
        void onSuccess(int currentScore);

        void onFailure();

        void onPerfect(int currentScore);
    }
}
