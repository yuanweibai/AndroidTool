package rango.tool.androidtool.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class RefreshRecyclerView extends LinearLayout {

    private static final int LAST_POSITION = 0;
    private RecyclerHeaderView headerView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private int headerViewHeight;
    private ValueAnimator currentRunningAnimator;


    private float downY;

    private RefreshStatus refreshStatus = RefreshStatus.NONE;

    private int distanceToRefresh;
    private int positionBackToLoadMore = LAST_POSITION;

    private boolean isLoadingMore = false;

    private OnRefreshListener onRefreshListener;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.refresh_recycler_view_layout, this);

        initView();
    }

    public void setDistanceToRefresh(int distance) {
        this.distanceToRefresh = distance;
    }

    public void setPositionBackToLoadMore(int positionBack) {
        this.positionBackToLoadMore = positionBack;
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.onRefreshListener = refreshListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isIntercept(e)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (onMove(e)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onUp();
                break;
        }
        return super.onTouchEvent(e);
    }

    private boolean isIntercept(MotionEvent event) {
        if (refreshStatus == RefreshStatus.REFRESHING) {
            return false;
        }
        float moveY = event.getY();
        float disY = moveY - downY;
        return (isFirstItemVisible() && disY > 0);
    }

    private boolean onMove(MotionEvent event) {
        if (!isFirstItemVisible()) {
            return false;
        }

        if (refreshStatus == RefreshStatus.REFRESHING
                || currentRunningAnimator != null) {
            return true;
        }

        if (isReadyToRefresh()) {
            if (refreshStatus != RefreshStatus.RELEASE_TO_REFRESH) {
                refreshStatus = RefreshStatus.RELEASE_TO_REFRESH;
                onPullToRefresh();
            }
        } else {
            if (refreshStatus != RefreshStatus.PULL_TO_REFRESH) {
                refreshStatus = RefreshStatus.PULL_TO_REFRESH;
                onReleaseToRefresh();
            }
        }

        float moveY = event.getY();
        int disY = (int) (moveY - downY);
        moveHeaderView(disY);

        return true;
    }

    private boolean isReadyToRefresh() {
        return Math.abs(getHeaderViewTopMargin() - getHeaderViewDefaultTopMargin()) >= distanceToRefresh;
    }

    private void onUp() {
        if (currentRunningAnimator != null) {
            return;
        }
        if (isReadyToRefresh()) {
            moveToRefreshPositionWithAnim();
        } else {
            resetHeaderViewWithAnim();
        }
    }

    private void onPullToRefresh() {
        headerView.onReleaseToRefresh();
    }

    private void onReleaseToRefresh() {
        headerView.onPullToRefresh();
    }

    private void onRefreshing() {
        headerView.onRefreshing();

        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    public void onRefreshed() {
        headerView.onRefreshed();
        resetHeaderViewWithAnim();
        refreshStatus = RefreshStatus.NONE;
    }

    private void moveToRefreshPositionWithAnim() {
        int currentTopMargin = getHeaderViewTopMargin();
        int purposeTopMargin = 0;

        long duration = getDuration(Math.abs(purposeTopMargin - currentTopMargin));

        cancelRunningAnimator();

        currentRunningAnimator = ValueAnimator.ofInt(currentTopMargin, purposeTopMargin);
        currentRunningAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            setHeaderViewTopMargin(value);
        });
        currentRunningAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (refreshStatus != RefreshStatus.REFRESHING) {
                    refreshStatus = RefreshStatus.REFRESHING;
                    onRefreshing();
                }

                currentRunningAnimator = null;
            }
        });
        currentRunningAnimator.setDuration(duration);
        currentRunningAnimator.start();
    }

    private void cancelRunningAnimator() {
        if (currentRunningAnimator != null) {
            currentRunningAnimator.cancel();
            currentRunningAnimator = null;
        }
    }

    private void resetHeaderViewWithAnim() {
        int currentTopMargin = getHeaderViewTopMargin();
        int defaultTopMargin = getHeaderViewDefaultTopMargin();
        long duration = getDuration(Math.abs(defaultTopMargin - currentTopMargin));

        cancelRunningAnimator();

        currentRunningAnimator = ValueAnimator.ofInt(currentTopMargin, defaultTopMargin);
        currentRunningAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            setHeaderViewTopMargin(value);
        });
        currentRunningAnimator.setDuration(duration);

        currentRunningAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentRunningAnimator = null;
            }
        });

        currentRunningAnimator.start();
    }

    private long getDuration(int distance) {
        return (long) (distance / (float) ScreenUtils.getScreenHeightPx() * 2000);
    }

    private boolean isFirstItemVisible() {
        return findFirstCompletelyVisibleItemPosition() == 0;
    }


    private void initView() {
        headerView = findViewById(R.id.header_view);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (isLoadingMore) {
                    return;
                }

                if (isCouldLoadMore()) {
                    isLoadingMore = true;
                    if (onRefreshListener != null) {
                        onRefreshListener.onLoadMore();
                    }
                }

            }
        });

    }

    private boolean isCouldLoadMore() {
        int lastVisiblePosition = findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= layoutManager.getItemCount() - 1 - positionBackToLoadMore;
    }

    private int findFirstCompletelyVisibleItemPosition() {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positionArray = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
            return positionArray[0];
        } else {
            throw new IllegalStateException("layoutManager is unknown!!!");
        }
    }

    private int findLastCompletelyVisibleItemPosition() {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positionArray = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);
            return positionArray[0];
        } else {
            throw new IllegalStateException("layoutManager is unknown!!!");
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (recyclerView != null) {
            recyclerView.addItemDecoration(decoration);
        }
    }

    public void scrollToPosition(int position) {
        if (recyclerView != null) {
            recyclerView.scrollToPosition(position);
        }
    }

    public void addHeaderView(HeaderView view) {
        if (headerView != null) {
            headerView.addView(view);

            headerView.setHeaderView(view);
            headerView.post(() -> {
                headerViewHeight = headerView.getHeight();
                distanceToRefresh = headerViewHeight;
                setHeaderViewTopMargin(-headerViewHeight);
            });
        }
    }

    private void setHeaderViewTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        params.topMargin = topMargin;
        headerView.setLayoutParams(params);
    }

    private void moveHeaderView(int disY) {
        int topMargin = -headerViewHeight + disY;
        setHeaderViewTopMargin(topMargin);
    }

    private int getHeaderViewTopMargin() {
        return ((LayoutParams) headerView.getLayoutParams()).topMargin;
    }

    private int getHeaderViewDefaultTopMargin() {
        return -headerViewHeight;
    }

    private enum RefreshStatus {
        NONE,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
