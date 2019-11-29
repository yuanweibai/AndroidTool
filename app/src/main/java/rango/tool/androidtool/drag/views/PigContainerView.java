package rango.tool.androidtool.drag.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

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

    private ImageView[][] pigImageViewArray = new AppCompatImageView[MAX_ROWS][MAX_COLUMNS];
    private RectF[][] pigRectfArray = new RectF[MAX_ROWS][MAX_COLUMNS];
    private ImageView touchPig = null;

    private Context context;

    public PigContainerView(Context context) {
        this(context, null);
    }

    public PigContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PigContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
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

        ImageView pigImageView = new AppCompatImageView(context);
        pigImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (row == 2) {
            pigImageView.setImageDrawable(getResources().getDrawable(R.drawable.pig_2));
        } else {
            pigImageView.setImageDrawable(getResources().getDrawable(R.drawable.pig_1));
        }

        ConstraintLayout.LayoutParams pigParams = new ConstraintLayout.LayoutParams(pigWidth, pigHeight);
        pigParams.leftToLeft = PIG_STY_IDS[row][column];
        pigParams.rightToRight = PIG_STY_IDS[row][column];
        pigParams.bottomToBottom = PIG_STY_IDS[row][column];
        pigParams.bottomMargin = pigBottomMargin;
        addView(pigImageView, -1, pigParams);

        pigImageViewArray[row][column] = pigImageView;
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
        final int action = event.getAction();
        final int pointerId = TouchEventUtils.getDefaultPointerId(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = TouchEventUtils.getX(event, pointerId);
                downY = TouchEventUtils.getY(event, pointerId);
                if (!setTouchPig(downX, downY)) {
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
        float tx = touchPig.getTranslationX();
        float ty = touchPig.getTranslationY();
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
        } else {
//            exchangePig(first, second);
            mergePig(first, second);
        }
    }

    private void exchangePig(int first, int second) {
        resetSelectPig();
        LayoutParams params = (LayoutParams) pigImageViewArray[first][second].getLayoutParams();
        LayoutParams selectParams = (LayoutParams) pigImageViewArray[selectPigIndex.first][selectPigIndex.second].getLayoutParams();
        pigImageViewArray[first][second].setLayoutParams(selectParams);
        pigImageViewArray[selectPigIndex.first][selectPigIndex.second].setLayoutParams(params);

        ImageView imageView = pigImageViewArray[first][second];
        pigImageViewArray[first][second] = pigImageViewArray[selectPigIndex.first][selectPigIndex.second];
        pigImageViewArray[selectPigIndex.first][selectPigIndex.second] = imageView;
    }

    private void mergePig(int first, int second) {
        pigImageViewArray[selectPigIndex.first][selectPigIndex.second].setVisibility(GONE);
        pigImageViewArray[selectPigIndex.first][selectPigIndex.second].setAlpha(1f);

        LayoutParams params = (LayoutParams) touchPig.getLayoutParams();
        params.leftMargin = (int) pigRectfArray[first][second].left;
        params.topMargin = (int) pigRectfArray[first][second].top;
        touchPig.setTranslationX(0);
        touchPig.setTranslationY(0);

        this.post(() -> startMergePigAnim(first, second));
    }

    private void startMergePigAnim(int first, int second) {
        float translation = ScreenUtils.dp2px(40);
        ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(touchPig, "translationX", 0f, translation, 0f);
        ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(pigImageViewArray[first][second], "translationX", 0f, -translation, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(firstAnimator, secondAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                touchPig.setAlpha(0.5f);
                pigImageViewArray[first][second].setAlpha(0.5f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pigImageViewArray[first][second].setAlpha(1f);
                pigImageViewArray[first][second].setImageDrawable(getResources().getDrawable(R.drawable.pig_2));

                touchPig.setAlpha(1f);
                hideTouchImageView();
            }
        });
        animatorSet.start();
    }

    private void resetSelectPig() {
        hideTouchImageView();
        pigImageViewArray[selectPigIndex.first][selectPigIndex.second].setAlpha(1f);
    }

    private void translationPig(float dx, float dy) {
        touchPig.setTranslationX(touchPig.getTranslationX() + dx);
        touchPig.setTranslationY(touchPig.getTranslationY() + dy);
    }

    private boolean setTouchPig(float x, float y) {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                RectF rectF = pigRectfArray[i][k];
                if (rectF.contains(x, y)) {
                    pigImageViewArray[i][k].setAlpha(0.5f);
                    resetTouchImageView(rectF, pigImageViewArray[i][k].getDrawable());
                    selectPigIndex = new Pair<>(i, k);
                    return true;
                }
            }
        }
        return false;
    }

    private void resetTouchImageView(RectF rectF, Drawable drawable) {
        ConstraintLayout.LayoutParams touchPigParams;
        if (touchPig == null) {
            touchPig = new AppCompatImageView(context);
            touchPig.setVisibility(GONE);
            touchPig.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            touchPigParams = new ConstraintLayout.LayoutParams(pigWidth, pigHeight);
            touchPigParams.leftToLeft = LayoutParams.PARENT_ID;
            touchPigParams.topToTop = LayoutParams.PARENT_ID;
            addView(touchPig, touchPigParams);
        } else {
            touchPigParams = (LayoutParams) touchPig.getLayoutParams();
        }
        touchPig.setImageDrawable(drawable);
        touchPigParams.leftMargin = (int) rectF.left;
        touchPigParams.topMargin = (int) rectF.top;
        touchPig.setVisibility(VISIBLE);
    }

    private void hideTouchImageView() {
        touchPig.setVisibility(GONE);
        touchPig.setTranslationY(0);
        touchPig.setTranslationX(0);
    }


    private void initPigRectf() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int k = 0; k < MAX_COLUMNS; k++) {
                ImageView pig = pigImageViewArray[i][k];
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
