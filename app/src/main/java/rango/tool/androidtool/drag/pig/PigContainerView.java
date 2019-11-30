package rango.tool.androidtool.drag.pig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;
import rango.tool.common.utils.TouchEventUtils;

public class PigContainerView extends ConstraintLayout {

    private static final int[][] PIG_STY_IDS = new int[][]{
            {R.id.pig_sty_0, R.id.pig_sty_1, R.id.pig_sty_2, R.id.pig_sty_3},
            {R.id.pig_sty_4, R.id.pig_sty_5, R.id.pig_sty_6, R.id.pig_sty_7},
            {R.id.pig_sty_8, R.id.pig_sty_9, R.id.pig_sty_10, R.id.pig_sty_11}
    };
    private static final int MAX_ROWS = 3;
    private static final int MAX_COLUMNS = 4;

    private int styWidth = ScreenUtils.dp2px(50);
    private int styHeight = ScreenUtils.dp2px(30);

    private int pigHeight = ScreenUtils.dp2px(70);
    private int pigWidth = ScreenUtils.dp2px(67.2f);
    private int pigBottomMargin = ScreenUtils.dp2px(10);
    private int pigTopMargin = ScreenUtils.dp2px(8);

    private PigView[][] pigViewArray = new PigView[MAX_ROWS][MAX_COLUMNS];
    private RectF[][] pigRectfArray = new RectF[MAX_ROWS][MAX_COLUMNS];
    private PigView flyPig = null; // be to move or anim or generate
    private ObjectAnimator flyAnimator;

    private boolean isMerging = false;

    private Context context;

    public PigContainerView(Context context) {
        this(context, null);
    }

    public PigContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PigContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipChildren(false);
        this.context = context;

        init();
    }

    public void generatePig(int grade, int x, int y) {
        final Pair<Integer, Integer> emptyStyPair = getEmptySty();
        if (emptyStyPair == null) {
            Toast.makeText(context, "猪圈已满！！！请杀猪吃肉。", Toast.LENGTH_SHORT).show();
            return;
        }

        pigViewArray[emptyStyPair.first][emptyStyPair.second].setTag(true);

        if (flyPig == null) {
            addFlyPigView();
        }


        if (flyAnimator != null) {
            flyAnimator.cancel();
            flyAnimator = null;
        }

        PigData data = new PigData();
        data.initData(grade);
        flyPig.setData(data);

        float startX = x - pigWidth / 2f;
        float startY = y - getTop() - pigHeight / 2f;

        RectF emptyStyRectF = pigRectfArray[emptyStyPair.first][emptyStyPair.second];

        float finalX = emptyStyRectF.left;
        float finalY = emptyStyRectF.top;

        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("translationX", startX, finalX);
        PropertyValuesHolder yHolder = PropertyValuesHolder.ofFloat("translationY", startY, finalY);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1f, 1f);
        flyAnimator = ObjectAnimator.ofPropertyValuesHolder(flyPig, xHolder, yHolder, alphaHolder);
        flyAnimator.setDuration(2000);
        flyAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        flyAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                flyPig.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PigView selectPigView = pigViewArray[emptyStyPair.first][emptyStyPair.second];
                selectPigView.setData(flyPig.getData());
                selectPigView.setVisibility(VISIBLE);
                hideFlyPig();
            }
        });
        flyAnimator.start();
    }

    private Pair<Integer, Integer> getEmptySty() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                if (!isHasPig(i, k)) {
                    return new Pair<>(i, k);
                }
            }
        }
        return null;
    }

    private void init() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                addStyAndPig(i, k);
            }
        }

        this.post(this::initPigRectf);
    }

    private void addStyAndPig(int row, int column) {
        ImageView styImageView = new AppCompatImageView(context);
        styImageView.setId(PIG_STY_IDS[row][column]);
        styImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        styImageView.setImageDrawable(getResources().getDrawable(R.drawable.pig_sty_bg));
        ConstraintLayout.LayoutParams styParams = new ConstraintLayout.LayoutParams(styWidth, styHeight);
        if (column == 0) {
            styParams.leftToLeft = LayoutParams.PARENT_ID;
        } else {
            styParams.leftToRight = PIG_STY_IDS[row][column - 1];
        }
        if (column == MAX_COLUMNS - 1) {
            styParams.rightToRight = LayoutParams.PARENT_ID;
        } else {
            styParams.rightToLeft = PIG_STY_IDS[row][column + 1];
        }
        if (row == 0) {
            styParams.topToTop = LayoutParams.PARENT_ID;
        } else {
            styParams.topToBottom = PIG_STY_IDS[row - 1][column];
        }
        styParams.topMargin = pigHeight + pigBottomMargin + pigTopMargin - styHeight;

        addView(styImageView, 0, styParams);

        PigView pigView = new PigView(context);
        pigView.setVisibility(INVISIBLE);

        ConstraintLayout.LayoutParams pigParams = new ConstraintLayout.LayoutParams(pigWidth, pigHeight);
        pigParams.leftToLeft = PIG_STY_IDS[row][column];
        pigParams.rightToRight = PIG_STY_IDS[row][column];
        pigParams.bottomToBottom = PIG_STY_IDS[row][column];
        pigParams.bottomMargin = pigBottomMargin;
        addView(pigView, -1, pigParams);

        pigViewArray[row][column] = pigView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // All touch event must be handle by myself
        return true;
    }

    private float downX;
    private float downY;
    private Pair<Integer, Integer> selectPigIndex;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isMerging) {
            return false;
        }
        final int action = event.getAction();
        final int pointerId = TouchEventUtils.getDefaultPointerId(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = TouchEventUtils.getX(event, pointerId);
                downY = TouchEventUtils.getY(event, pointerId);
                if (!tryToMovePig(downX, downY)) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = TouchEventUtils.getX(event, pointerId);
                float moveY = TouchEventUtils.getY(event, pointerId);
                translationPig(moveX - downX, moveY - downY);
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                tryToUpgradePig();
                break;
        }
        return true;
    }

    private void tryToUpgradePig() {
        float tx = flyPig.getTranslationX();
        float ty = flyPig.getTranslationY();
        RectF rectF = pigRectfArray[selectPigIndex.first][selectPigIndex.second];
        float finalLX = rectF.left + tx;
        float finalTY = rectF.top + ty;

        int first = 0;
        int second = 0;
        float sx = finalLX - pigRectfArray[first][second].left;
        float sy = finalTY - pigRectfArray[first][second].top;
        float distance = sx * sx + sy * sy;

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                if (i == 0 && k == 0) {
                    continue;
                }
                float x = finalLX - pigRectfArray[i][k].left;
                float y = finalTY - pigRectfArray[i][k].top;
                float testDistance = x * x + y * y;
                if (distance > testDistance) {
                    distance = testDistance;
                    first = i;
                    second = k;
                }
            }
        }

        if (first == selectPigIndex.first && second == selectPigIndex.second) {
            resetSelectPig();
        } else if (!isHasPig(first, second)) {
            hideFlyPig();

            PigView selectPigView = pigViewArray[selectPigIndex.first][selectPigIndex.second];
            showPigView(pigViewArray[first][second], selectPigView.getData());

            releasePigView(selectPigView);
        } else {
            handlePig(first, second);
        }
    }

    private void handlePig(int first, int second) {
        PigView selectPigView = pigViewArray[selectPigIndex.first][selectPigIndex.second];
        PigView pigView = pigViewArray[first][second];
        if (selectPigView.equals(pigView)) {
            mergePig(first, second);
        } else {
            exchangePig(first, second);
        }
    }

    private void exchangePig(int first, int second) {
        resetSelectPig();
        LayoutParams params = (LayoutParams) pigViewArray[first][second].getLayoutParams();
        LayoutParams selectParams = (LayoutParams) pigViewArray[selectPigIndex.first][selectPigIndex.second].getLayoutParams();
        pigViewArray[first][second].setLayoutParams(selectParams);
        pigViewArray[selectPigIndex.first][selectPigIndex.second].setLayoutParams(params);

        PigView tempPigView = pigViewArray[first][second];
        pigViewArray[first][second] = pigViewArray[selectPigIndex.first][selectPigIndex.second];
        pigViewArray[selectPigIndex.first][selectPigIndex.second] = tempPigView;
    }

    private void mergePig(int first, int second) {

        isMerging = true;
        PigView selectPigView = pigViewArray[selectPigIndex.first][selectPigIndex.second];
        selectPigView.setTag(false);
        selectPigView.setVisibility(GONE);
        selectPigView.setAlpha(1f);

        LayoutParams params = (LayoutParams) flyPig.getLayoutParams();
        params.leftMargin = (int) pigRectfArray[first][second].left;
        params.topMargin = (int) pigRectfArray[first][second].top;
        flyPig.setTranslationX(0);
        flyPig.setTranslationY(0);


        this.post(() -> startMergePigAnim(first, second, () -> isMerging = false));
    }

    private void startMergePigAnim(int first, int second, Runnable endAction) {
        float translation = ScreenUtils.dp2px(40);
        ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(flyPig, "translationX", 0f, translation, 0f);
        ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(pigViewArray[first][second], "translationX", 0f, -translation, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(firstAnimator, secondAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                flyPig.setAlpha(0.5f);
                pigViewArray[first][second].setAlpha(0.5f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pigViewArray[first][second].setAlpha(1f);
                pigViewArray[first][second].upgrade();

                flyPig.setAlpha(1f);
                hideFlyPig();
                if (endAction != null) {
                    endAction.run();
                }
            }
        });
        animatorSet.start();
    }

    private void resetSelectPig() {
        hideFlyPig();
        pigViewArray[selectPigIndex.first][selectPigIndex.second].setAlpha(1f);
    }

    private void releasePigView(PigView pigView) {
        pigView.setVisibility(INVISIBLE);
        pigView.setAlpha(1f);
        pigView.setTag(false);
        pigView.release();
    }

    private void showPigView(PigView pigView, PigData data) {
        pigView.setData(data);
        pigView.setVisibility(VISIBLE);
        pigView.setTag(true);
    }

    private void translationPig(float dx, float dy) {
        flyPig.setTranslationX(flyPig.getTranslationX() + dx);
        flyPig.setTranslationY(flyPig.getTranslationY() + dy);
    }

    private boolean tryToMovePig(float x, float y) {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                RectF rectF = pigRectfArray[i][k];
                if (rectF.contains(x, y)) {
                    if (!isHasPig(i, k)) {
                        return false;
                    }
                    pigViewArray[i][k].setAlpha(0.5f);
                    resetFlyPigView(rectF, pigViewArray[i][k].getData());
                    selectPigIndex = new Pair<>(i, k);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isHasPig(int x, int y) {
        Object tag = pigViewArray[x][y].getTag();
        return tag instanceof Boolean && (boolean) tag;
    }

    private void resetFlyPigView(RectF rectF, PigData pigData) {
        if (flyPig == null) {
            addFlyPigView();
        }
        ConstraintLayout.LayoutParams touchPigParams = (LayoutParams) flyPig.getLayoutParams();
        flyPig.setData(pigData);
        touchPigParams.leftMargin = (int) rectF.left;
        touchPigParams.topMargin = (int) rectF.top;
        flyPig.setVisibility(VISIBLE);
    }

    private void addFlyPigView() {
        flyPig = new PigView(context);
        flyPig.setVisibility(GONE);
        ConstraintLayout.LayoutParams touchPigParams = new ConstraintLayout.LayoutParams(pigWidth, pigHeight);
        touchPigParams.leftToLeft = LayoutParams.PARENT_ID;
        touchPigParams.topToTop = LayoutParams.PARENT_ID;
        addView(flyPig, touchPigParams);
    }

    private void hideFlyPig() {
        flyPig.setVisibility(GONE);
        flyPig.setTranslationY(0);
        flyPig.setTranslationX(0);
        ConstraintLayout.LayoutParams params = (LayoutParams) flyPig.getLayoutParams();
        params.leftMargin = 0;
        params.topMargin = 0;
    }

    private void initPigRectf() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                ImageView pig = pigViewArray[i][k];
                RectF rectF = new RectF();
                rectF.left = pig.getLeft();
                rectF.top = pig.getTop();
                rectF.right = pig.getRight();
                rectF.bottom = pig.getBottom();
                pigRectfArray[i][k] = rectF;
            }
        }
    }
}
