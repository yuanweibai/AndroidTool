package rango.tool.androidtool.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.viewpager.ViewPagerAdapter;
import rango.tool.androidtool.view.util.BannerScroller;
import rango.tool.common.utils.ScreenUtils;
import rango.tool.common.utils.Worker;

public abstract class AutoScrollCircleLayout<T> extends ConstraintLayout {

    private static final String TAG = "AutoScrollCircleLayout";

    private static final boolean DEFAULT_AUTO_PLAY = true;
    private static final long DEFAULT_SCROLL_INTERVAL = 3000;

    private long scrollInterval;

    protected int count;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean isAutoPlay;
    private int currentPosition;
    private int realCurrentPosition;
    private int currentState = ViewPager.SCROLL_STATE_IDLE;
    private boolean isDragged;
    private boolean isTaskRunning;
    private boolean isTemporaryStopAutoScroll = false;
    private LinearLayout indicatorContainerView;

    public AutoScrollCircleLayout(Context context) {
        this(context, null);
    }

    public AutoScrollCircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoScrollCircleLayout, defStyleAttr, 0);
        isAutoPlay = ta.getBoolean(R.styleable.AutoScrollCircleLayout_auto_play, DEFAULT_AUTO_PLAY);
        ta.recycle();
        LayoutInflater.from(context).inflate(R.layout.auto_scroll_layout, this);
        scrollInterval = DEFAULT_SCROLL_INTERVAL;
        initView();
    }

    @SuppressWarnings("unused")
    public void setScrollInterval(long interval) {
        scrollInterval = interval;
    }

    public void startAutoPlay() {
        this.isAutoPlay = true;
        autoPlay();
    }

    public void stopAutoPlay() {
        removeAutoScrollTask();
        this.isAutoPlay = false;
    }

    public void setData(List<T> data) {
        count = data.size();
        initIndicatorView();
        handleData(data);
        List<View> viewList = getItemData(data);
        setAdapter(viewList);
        if (isAutoPlay) {
            autoPlay();
        }
    }

    protected abstract List<View> getItemData(List<T> data);

    protected void setAdapter(List<View> viewList) {
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(viewList);
            viewPager.setAdapter(viewPagerAdapter);
        } else {
            viewPagerAdapter.update(viewList);
        }
        setCurrentItem(1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            autoPlayAfterTemporaryStop();
        } else {
            temporaryStopPlay();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initIndicatorView() {
        if (count < 1) {
            return;
        }

        if (indicatorContainerView != null) {
            removeView(indicatorContainerView);
            indicatorContainerView = null;
        }

        indicatorContainerView = new LinearLayout(getContext());
        indicatorContainerView.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < count; i++) {
            View view = new View(getContext());
            if (i == 0) {
                view.setBackgroundColor(Color.WHITE);
            } else {
                view.setBackgroundColor(Color.RED);
            }

            int size = ScreenUtils.dp2px(5);
            int margin = ScreenUtils.dp2px(6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.leftMargin = margin;
            params.rightMargin = margin;
            indicatorContainerView.addView(view, params);
        }

        LayoutParams indicatorParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicatorParams.leftToLeft = LayoutParams.PARENT_ID;
        indicatorParams.rightToRight = LayoutParams.PARENT_ID;
        indicatorParams.bottomToBottom = LayoutParams.PARENT_ID;

        indicatorParams.bottomMargin = ScreenUtils.dp2px(12);

        addView(indicatorContainerView, indicatorParams);
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                int realPosition = getRealPosition();
                onRealPageSelected(realPosition);
                Log.e(TAG, "select_position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "scroll_state: " + state);
                currentState = state;
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        resetPosition();
                        autoPlayAfterDragging();
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isDragged = true;
                        removeAutoScrollTask();
                        resetPosition();
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        initViewPagerScroller();
    }

    private void onRealPageSelected(int realPosition) {
        if (realCurrentPosition == realPosition) {
            Log.e(TAG, "real_position = " + realCurrentPosition + ", but repetitive!!!");
            return;
        }

        int lastRealPosition = realCurrentPosition;

        realCurrentPosition = realPosition;
        Log.e(TAG, "real_position = " + realCurrentPosition);

        indicatorContainerView.getChildAt(realCurrentPosition).setBackgroundColor(Color.WHITE);
        indicatorContainerView.getChildAt(lastRealPosition).setBackgroundColor(Color.RED);
    }

    private void initViewPagerScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            BannerScroller scroller = new BannerScroller(getContext());
            mField.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void resetPosition() {
        if (currentPosition == count + 1) {
            setCurrentItem(1);
        } else if (currentPosition == 0) {
            setCurrentItem(count);
        }
    }

    private int getRealPosition() {
        if (currentPosition == count + 1) {
            return 0;
        } else if (currentPosition == 0) {
            return count - 1;
        } else {
            return currentPosition - 1;
        }
    }

    private void autoPlayAfterDragging() {
        if (isDragged && isAutoPlay) {
            isDragged = false;
            autoPlay();
        }
    }

    private void autoPlayAfterTemporaryStop() {
        isTemporaryStopAutoScroll = false;
        if (isAutoPlay) {
            autoPlay();
        }
    }

    private void temporaryStopPlay() {
        if (isTemporaryStopAutoScroll) {
            return;
        }
        isTemporaryStopAutoScroll = true;
        removeAutoScrollTask();
    }

    private void removeAutoScrollTask() {
        if (isTaskRunning) {
            isTaskRunning = false;
            Worker.removeMain(autoScrollTask);
        }
    }

    private void autoPlay() {
        if (!isTaskRunning) {
            isTaskRunning = true;
            Worker.postMain(autoScrollTask, scrollInterval);
        }
    }

    private boolean isCouldRunningTask() {
        return currentState != ViewPager.SCROLL_STATE_DRAGGING && !isTemporaryStopAutoScroll;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }

    private void setCurrentItemWithAnim(int position) {
        viewPager.setCurrentItem(position);
    }

    private void handleData(List<T> data) {
        if (data.size() > 1) {
            T f = data.get(0);
            T l = data.get(data.size() - 1);
            data.add(0, l);
            data.add(f);
        }
    }

    private final Runnable autoScrollTask = new Runnable() {
        @Override
        public void run() {
            isTaskRunning = false;
            if (count > 1 && isAutoPlay && isCouldRunningTask()) {
                int position = currentPosition % (count + 1) + 1;
                setCurrentItemWithAnim(position);
                autoPlay();
            }
        }
    };
}
