package rango.tool.androidtool.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.adapter.ViewPagerAdapter;
import rango.tool.androidtool.view.util.BannerScroller;
import rango.tool.common.utils.Worker;

public abstract class AutoScrollCircleLayout<T> extends RelativeLayout {

    private static final String TAG = AutoScrollCircleLayout.class.getSimpleName();
    private static final boolean DEFAULT_AUTO_PLAY = true;
    private static final long DURATION = 3000;
    protected int count;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean isAutoPlay;
    private int currentPosition;
    private int currentState = ViewPager.SCROLL_STATE_IDLE;
    private boolean isDragged;
    private boolean isTaskRunning;
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
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "select_position: " + position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "scroll_state: " + state);
                currentState = state;
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        resetPosition();
                        couldAutoPlay();
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isDragged = true;
                        removeTask();
                        resetPosition();
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        initViewPagerScroll();
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            BannerScroller scroller = new BannerScroller(getContext());
            mField.set(viewPager, scroller);
        } catch (NoSuchFieldException ignored) {

        } catch (IllegalAccessException ignored) {

        }
    }

    private void resetPosition() {
        if (currentPosition == count + 1) {
            setCurrentItem(1);
        } else if (currentPosition == 0) {
            setCurrentItem(count);
        }
    }

    private void couldAutoPlay() {
        if (isDragged && isAutoPlay) {
            isDragged = false;
            autoPlay();
        }
    }

    private void removeTask() {
        if (isTaskRunning) {
            isTaskRunning = false;
            Worker.removeMain(autoScrollTask);
        }

    }

    protected void setAdapter(List<View> viewList) {
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(viewList);
            viewPager.setAdapter(viewPagerAdapter);
        } else {
            viewPagerAdapter.update(viewList);
        }
        setCurrentItem(1);
    }

    public void startAutoPlay() {
        this.isAutoPlay = true;
        autoPlay();
    }

    public void stopAutoPlay() {
        this.isAutoPlay = false;
    }

    private void autoPlay() {
        if (!isTaskRunning) {
            isTaskRunning = true;
            Worker.postMain(autoScrollTask, DURATION);
        }
    }

    private boolean isCouldRunningTask() {
        if (currentState != ViewPager.SCROLL_STATE_DRAGGING) {
            return true;
        }
        return false;
    }


    protected void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }

    private void setCurrentItemWithAnim(int position) {
        viewPager.setCurrentItem(position);
    }

    protected void handleData(List<T> data) {
        if (data.size() > 1) {
            T f = data.get(0);
            T l = data.get(data.size() - 1);
            data.add(0, l);
            data.add(f);
        }
    }

    public void setData(List<T> data) {
        count = data.size();
        handleData(data);
        List<View> viewList = getItemData(data);
        setAdapter(viewList);
        if (isAutoPlay) {
            autoPlay();
        }
    }

    protected abstract List<View> getItemData(List<T> data);
}
