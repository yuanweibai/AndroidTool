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
    private static final int STATUS_START = 1;
    private static final int STATUS_IDLE = 2;
    private static final int STATUS_BRIDGE_GROWING = 3;
    private static final int STATUS_BRIDGE_ROTATING = 4;
    private static final int STATUS_PERSON_KICK = 5;
    private static final int STATUS_PERSON_WALKING = 6;
    private static final int STATUS_SHOW_NEXT_LEVEL = 7;
    private static final int STATUS_FAILURE = 8;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private float FIRST_PILLAR_LEFT_MIN_DISTANCE;

    private float PILLAR_TOP;
    private float PILLAR_BOTTOM;
    private float PILLAR_HEIGHT;

    @IntDef({STATUS_INIT, STATUS_START, STATUS_IDLE, STATUS_BRIDGE_GROWING,
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

    private Coordinate personBodyCoord;

    private Coordinate firstPillarCoord;
    private Coordinate secondPillarCoord;
    private Coordinate thirdPillarCoord;

    private float lastBridgeLength = 0;
    private float currentBridgeLength = 0;
    private float BRIDGE_MAX_LENGTH;
    private int currentBridgeRotateAngle = 0;

    private float lastWalkDistanceForLeg;
    private float currentWalkDistance = 0;
    private float lastWalkDistance = 0;

    private float lastShowNextLevelMoveRatio = 0f;
    private float currentShowNextLevelMoveRatio = 0f;

    private float lastFallDistance = 0;
    private float currentFallDistance = 0f;

    private float currentPillarsXMaxDistance;
    private float thirdPillarXMaxDistance;

    private float lastStartMoveRatio = 0;
    private float currentStartMoveRatio = 0;

    private float personXMaxDistance = 0f;
    private float personYMaxDistance = 0f;
    private float firstPillarXMaxDistance = 0f;
    private float firstPillarYMaxDistance = 0f;
    private float secondPillarXMaxDistance = 0f;

    private int currentScore = 0;

    private boolean isFlag = false;

    private ValueAnimator bridgeRotateAnimator;
    private ValueAnimator walkAnimator;
    private ValueAnimator levelAnimator;
    private ValueAnimator fallAnimator;
    private ValueAnimator startAnimator;

    private OnStatusListener statusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        statusListener = listener;
    }

    public void start() {
        currentScore = 0;
        lastStartMoveRatio = 0f;
        currentStartMoveRatio = 0f;
        personXMaxDistance = SCREEN_WIDTH / 2f - (getInitPillarWidth() - PERSON_BACKUP_DISTANCE - PERSON_BODY_WIDTH / 2f);
        personYMaxDistance = PILLAR_HEIGHT - getInitPillarHeight();
        firstPillarXMaxDistance = SCREEN_WIDTH / 2f - getInitPillarWidth() / 2f;
        firstPillarYMaxDistance = personYMaxDistance;

        float intersticeDistance = getIntersticeDistance();
        float secondPillarWidth = getPillarWidth();

        if (firstPillarCoord.r - firstPillarXMaxDistance + intersticeDistance + secondPillarWidth > SCREEN_WIDTH) {
            intersticeDistance = (SCREEN_WIDTH - (firstPillarCoord.r - firstPillarXMaxDistance)) / 3f;
            secondPillarWidth = intersticeDistance * 2;
        }

        secondPillarCoord.l = SCREEN_WIDTH;
        secondPillarCoord.t = PILLAR_TOP;
        secondPillarCoord.r = secondPillarCoord.l + secondPillarWidth;
        secondPillarCoord.b = PILLAR_BOTTOM;
        secondPillarXMaxDistance = SCREEN_WIDTH - (firstPillarCoord.r - firstPillarXMaxDistance + intersticeDistance);

        if (startAnimator == null) {
            startAnimator = ValueAnimator.ofFloat(0f, 1f);
            startAnimator.addUpdateListener(animation -> {
                lastStartMoveRatio = currentStartMoveRatio;
                currentStartMoveRatio = (float) animation.getAnimatedValue();
                invalidate();
            });
            startAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    onStartEnd();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onStartEnd();
                }
            });
            startAnimator.setDuration(GameHeroConstants.START_DURATION);
            startAnimator.setInterpolator(new LinearInterpolator());
        }
        currentStatus = STATUS_START;
        startAnimator.start();

    }

    public void retry() {
        float firstPillarWidth = getPillarWidth();
        if (firstPillarWidth < FIRST_PILLAR_LEFT_MIN_DISTANCE) {
            firstPillarCoord.l = FIRST_PILLAR_LEFT_MIN_DISTANCE - firstPillarWidth;
        } else {
            firstPillarCoord.l = 0;
        }
        firstPillarCoord.t = PILLAR_TOP;
        firstPillarCoord.r = firstPillarCoord.l + firstPillarWidth;
        firstPillarCoord.b = PILLAR_BOTTOM;

        float interstice = getIntersticeDistance();
        float secondPillarWidth = getPillarWidth();
        if (firstPillarCoord.r + interstice + secondPillarWidth > SCREEN_WIDTH) {
            interstice = (SCREEN_WIDTH - (firstPillarCoord.r - firstPillarXMaxDistance)) / 3f;
            secondPillarWidth = interstice * 2;
        }

        secondPillarCoord.l = firstPillarCoord.r + interstice;
        secondPillarCoord.t = PILLAR_TOP;
        secondPillarCoord.r = secondPillarCoord.l + secondPillarWidth;
        secondPillarCoord.b = PILLAR_BOTTOM;

        currentContentTranslationX = 0;
        currentScore = 0;
        currentStatus = STATUS_IDLE;
        invalidate();
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

        initStatus();
    }

    private void increaseScoreWhenPassedOneLevel() {
        currentScore += GameHeroConstants.GAME_SCORE_EVERY_LEVEL;
        if (statusListener != null) {
            statusListener.onSuccess(currentScore);
        }
    }

    private void increaseScoreWhenPassedPerfectly() {
        currentScore += GameHeroConstants.GAME_SCORE_PERFECTLY;
        if (statusListener != null) {
            statusListener.onPerfect(currentScore);
        }
    }

    private void initStatus() {
        currentStatus = STATUS_INIT;
        personBodyCoord = new Coordinate();
        firstPillarCoord = new Coordinate();
        secondPillarCoord = new Coordinate();
        thirdPillarCoord = new Coordinate();

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

            float pillarWidth = getInitPillarWidth();
            float pillarHeight = getInitPillarHeight();

            firstPillarCoord.l = SCREEN_WIDTH / 2f - pillarWidth / 2f;
            firstPillarCoord.t = SCREEN_HEIGHT - pillarHeight;
            firstPillarCoord.r = firstPillarCoord.l + pillarWidth;
            firstPillarCoord.b = firstPillarCoord.t + pillarHeight;

            drawPillarCoordinate(canvas, firstPillarCoord);
        } else if (currentStatus == STATUS_START) {
            float diff = currentStartMoveRatio - lastStartMoveRatio;
            float xDistance = diff * firstPillarXMaxDistance;
            float yDistance = diff * firstPillarYMaxDistance;
            firstPillarCoord.l -= xDistance;
            firstPillarCoord.r -= xDistance;
            firstPillarCoord.t -= yDistance;
            drawPillarCoordinate(canvas, firstPillarCoord);

            float secondXDistance = diff * secondPillarXMaxDistance;
            secondPillarCoord.l -= secondXDistance;
            secondPillarCoord.r -= secondXDistance;
            drawPillarCoordinate(canvas, secondPillarCoord);
        } else if (currentStatus == STATUS_IDLE
                || currentStatus == STATUS_BRIDGE_GROWING
                || currentStatus == STATUS_BRIDGE_ROTATING
                || currentStatus == STATUS_PERSON_KICK
                || currentStatus == STATUS_PERSON_WALKING
                || currentStatus == STATUS_FAILURE) {

            drawPillarCoordinate(canvas, firstPillarCoord);
            drawPillarCoordinate(canvas, secondPillarCoord);

        } else if (currentStatus == STATUS_SHOW_NEXT_LEVEL) {
            float currentPillarsMoveDistance = getCurrentPillarsMoveDistance();
            firstPillarCoord.l -= currentPillarsMoveDistance;
            firstPillarCoord.r -= currentPillarsMoveDistance;

            secondPillarCoord.l -= currentPillarsMoveDistance;
            secondPillarCoord.r -= currentPillarsMoveDistance;

            float thirdPillarMoveDistance = (currentShowNextLevelMoveRatio - lastShowNextLevelMoveRatio) * thirdPillarXMaxDistance;
            thirdPillarCoord.l -= thirdPillarMoveDistance;
            thirdPillarCoord.r -= thirdPillarMoveDistance;

            drawPillarCoordinate(canvas, firstPillarCoord);
            drawPillarCoordinate(canvas, secondPillarCoord);
            drawPillarCoordinate(canvas, thirdPillarCoord);
        }
    }

    private void drawPillarCoordinate(Canvas canvas, Coordinate pillar) {
        if (pillar.r <= 0) {
            return;
        }
        paint.setColor(Color.BLACK);
        canvas.drawRect(pillar.l, pillar.t, pillar.r, pillar.b, paint);

        float cL = pillar.l + (pillar.r - pillar.l - PERFECT_RECT_WIDTH) / 2f;
        float cR = cL + PERFECT_RECT_WIDTH;
        float cB = pillar.t + PERFECT_RECT_HEIGHT;
        paint.setColor(Color.RED);
        canvas.drawRect(cL, pillar.t, cR, cB, paint);
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
        if (currentStatus == STATUS_INIT) {
            personBodyCoord.l = SCREEN_WIDTH / 2f - PERSON_BODY_WIDTH / 2f;
            personBodyCoord.t = SCREEN_HEIGHT - SCREEN_HEIGHT * GameHeroConstants.PILLAR_INIT_HEIGHT_RATIO - PERSON_LEG_HEIGHT - PERSON_BODY_HEIGHT;
            personBodyCoord.r = personBodyCoord.l + PERSON_BODY_WIDTH;
            personBodyCoord.b = personBodyCoord.t + PERSON_BODY_HEIGHT;
        } else if (currentStatus == STATUS_START) {
            float diff = currentStartMoveRatio - lastStartMoveRatio;
            personBodyCoord.l -= diff * personXMaxDistance;
            personBodyCoord.t -= diff * personYMaxDistance;
            personBodyCoord.r = personBodyCoord.l + PERSON_BODY_WIDTH;
            personBodyCoord.b = personBodyCoord.t + PERSON_BODY_HEIGHT;
        } else if (currentStatus == STATUS_PERSON_WALKING) {
            personBodyCoord.l = firstPillarCoord.r - PERSON_BACKUP_DISTANCE - PERSON_BODY_WIDTH + currentWalkDistance;
            personBodyCoord.t = PILLAR_TOP - PERSON_LEG_HEIGHT - PERSON_BODY_HEIGHT;
            personBodyCoord.r = personBodyCoord.l + PERSON_BODY_WIDTH;
            personBodyCoord.b = personBodyCoord.t + PERSON_BODY_HEIGHT;
        } else if (currentStatus == STATUS_FAILURE) {
            float diff = currentFallDistance - lastFallDistance;
            personBodyCoord.t += diff;
            personBodyCoord.b += diff;
        } else if (currentStatus == STATUS_SHOW_NEXT_LEVEL) {
            float diff = getCurrentPillarsMoveDistance();
            personBodyCoord.l -= diff;
            personBodyCoord.r -= diff;
        } else if (currentStatus == STATUS_BRIDGE_GROWING
                || currentStatus == STATUS_IDLE
                || currentStatus == STATUS_PERSON_KICK
                || currentStatus == STATUS_BRIDGE_ROTATING) {
            personBodyCoord.l = firstPillarCoord.r - PERSON_BACKUP_DISTANCE - PERSON_BODY_WIDTH;
            personBodyCoord.t = firstPillarCoord.t - PERSON_LEG_HEIGHT - PERSON_BODY_HEIGHT;
            personBodyCoord.r = personBodyCoord.l + PERSON_BODY_WIDTH;
            personBodyCoord.b = personBodyCoord.t + PERSON_BODY_HEIGHT;
        }

        if (currentStatus == STATUS_BRIDGE_GROWING) {

            if (currentBridgeLength - lastBridgeLength > 90) {
                lastBridgeLength = currentBridgeLength;
                isFlag = !isFlag;
            }

            if (!isFlag) {
                personBodyCoord.l -= PERSON_SCALE_OFFSET;
                personBodyCoord.r += PERSON_SCALE_OFFSET;
                personBodyCoord.t += PERSON_SCALE_OFFSET;
            }
        }

        float radius = PERSON_EYE_SIZE / 2f;
        float cx = personBodyCoord.r - PERSON_EYE_RIGHT_MARGIN - radius;
        float cy = personBodyCoord.t + PERSON_EYE_TOP_MARGIN + radius;

        paint.setStrokeWidth(ScreenUtils.dp2px(2));
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(personBodyCoord.l, personBodyCoord.t, personBodyCoord.r, personBodyCoord.b, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, radius, paint);

        float startX1 = personBodyCoord.l + PERSON_LEG_OFFSET;
        float stopY1 = personBodyCoord.b + PERSON_LEG_HEIGHT;
        float stopX1 = startX1;

        float startX2 = personBodyCoord.r - PERSON_LEG_OFFSET;
        float stopX2 = startX2;
        float stopY2 = personBodyCoord.b + PERSON_LEG_HEIGHT;

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
        canvas.drawLine(startX1, personBodyCoord.b, stopX1, stopY1, paint);
        canvas.drawLine(startX2, personBodyCoord.b, stopX2, stopY2, paint);
    }

    private void drawBridge(Canvas canvas) {
        if (currentStatus == STATUS_IDLE || currentStatus == STATUS_INIT || currentStatus == STATUS_START) {
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(BRIDGE_WIDTH);
        float startX = firstPillarCoord.r - BRIDGE_BACKUP_DISTANCE - BRIDGE_WIDTH / 2f;
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
        isFlag = false;
        lastWalkDistanceForLeg = 0;
        if (walkAnimator != null) {
            walkAnimator.cancel();
            walkAnimator = null;
        }

        boolean isPass;
        float walkDistance;
        float extraDis = BRIDGE_BACKUP_DISTANCE + BRIDGE_WIDTH / 2f;
        float correctMaxDistance = secondPillarCoord.r - firstPillarCoord.r + extraDis;
        float correctMinDistance = secondPillarCoord.l - firstPillarCoord.r + extraDis;
        if (currentBridgeLength <= correctMaxDistance && currentBridgeLength >= correctMinDistance) {
            isPass = true;
            walkDistance = correctMaxDistance - extraDis;
            float secondPillarWidth = secondPillarCoord.r - secondPillarCoord.l;
            float perfectMaxDistance = (secondPillarCoord.r - secondPillarWidth / 2f + PERFECT_RECT_WIDTH / 2f) - firstPillarCoord.r + extraDis;
            float perfectMinDistance = (secondPillarCoord.r - secondPillarWidth / 2f - PERFECT_RECT_WIDTH / 2f) - firstPillarCoord.r + extraDis;
            if (currentBridgeLength <= perfectMaxDistance && currentBridgeLength >= perfectMinDistance) {
                increaseScoreWhenPassedPerfectly();
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
        currentStatus = STATUS_PERSON_WALKING;
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
        lastShowNextLevelMoveRatio = 0f;
        currentShowNextLevelMoveRatio = 0f;

        currentPillarsXMaxDistance = secondPillarCoord.l;
        float maxD = secondPillarCoord.r - FIRST_PILLAR_LEFT_MIN_DISTANCE;
        if (currentPillarsXMaxDistance > maxD) {
            currentPillarsXMaxDistance = maxD;
        }

        float intersticeDistance = getIntersticeDistance();
        float pillarWidth = getPillarWidth();

        if (secondPillarCoord.r - currentPillarsXMaxDistance + intersticeDistance + pillarWidth > SCREEN_WIDTH) {
            intersticeDistance = (SCREEN_WIDTH - (secondPillarCoord.r - currentPillarsXMaxDistance)) / 3f;
            pillarWidth = intersticeDistance * 2;
        }

        thirdPillarCoord.l = SCREEN_WIDTH;
        thirdPillarCoord.t = PILLAR_TOP;
        thirdPillarCoord.r = thirdPillarCoord.l + pillarWidth;
        thirdPillarCoord.b = PILLAR_BOTTOM;

        thirdPillarXMaxDistance = SCREEN_WIDTH - (secondPillarCoord.r - currentPillarsXMaxDistance + intersticeDistance);

        if (levelAnimator == null) {
            levelAnimator = ValueAnimator.ofFloat(0f, 1f);
            levelAnimator.addUpdateListener(animation -> {
                lastShowNextLevelMoveRatio = currentShowNextLevelMoveRatio;
                currentShowNextLevelMoveRatio = (float) animation.getAnimatedValue();
                invalidate();
            });

            levelAnimator.addListener(new AnimatorListenerAdapter() {

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
            levelAnimator.setInterpolator(new LinearInterpolator());
        }

        long duration = (long) (currentPillarsXMaxDistance / GameHeroConstants.GO_TO_NEXT_LEVEL_SPEED * 1000);
        levelAnimator.setDuration(duration);
        currentStatus = STATUS_SHOW_NEXT_LEVEL;
        levelAnimator.start();
    }

    private float getCurrentPillarsMoveDistance() {
        return (currentShowNextLevelMoveRatio - lastShowNextLevelMoveRatio) * currentPillarsXMaxDistance;
    }

    private void showNextLevelComplete() {
        increaseScoreWhenPassedOneLevel();
        Coordinate temp = firstPillarCoord;
        firstPillarCoord = secondPillarCoord;
        secondPillarCoord = thirdPillarCoord;
        thirdPillarCoord = temp;

        currentStatus = STATUS_IDLE;
    }

    private void startFall() {
        currentStatus = STATUS_FAILURE;
        currentFallDistance = 0f;
        lastFallDistance = 0f;
        if (fallAnimator == null) {
            fallAnimator = ValueAnimator.ofFloat(0, PILLAR_HEIGHT + PERSON_BODY_HEIGHT + PERSON_LEG_HEIGHT);
            fallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    lastFallDistance = currentFallDistance;
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

    private void onStartEnd() {
        post(() -> currentStatus = STATUS_IDLE);
    }

    private void onFallEnd() {
        if (statusListener != null) {
            statusListener.onFailure();
        }
    }

    private float getInitPillarWidth() {
        return PERFECT_RECT_WIDTH * GameHeroConstants.PILLAR_INIT_WIDTH_MULTIPLE;
    }

    private float getInitPillarHeight() {
        return SCREEN_HEIGHT * GameHeroConstants.PILLAR_INIT_HEIGHT_RATIO;
    }

    public interface OnStatusListener {
        void onSuccess(int currentScore);

        void onFailure();

        void onPerfect(int currentScore);
    }

    private class Coordinate {
        private float l;
        private float t;
        private float r;
        private float b;

        private boolean isEnable = false;

        private boolean isEnable() {
            return isEnable;
        }
    }
}
