package rango.tool.androidtool.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import rango.tool.androidtool.R;

public class CustomListView extends ListView implements AbsListView.OnScrollListener {

    private HeaderViewHolder headerViewHolder;
    private FooterViewHolder footerViewHolder;
    private RefreshState refreshState;

    private RefreshListener refreshListener;

    private boolean isLoadingMore = false;
    private boolean isShowingFooter = false;
    private int loadMoreCount = 0;

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public CustomListView(Context context) {
        this(context, null);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOnScrollListener(this);
        headerViewHolder = initHeaderView();
        footerViewHolder = initFooterView();
        setRefreshState(RefreshState.PULL_TO_REFRESH);
    }

    private HeaderViewHolder initHeaderView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_header_layout, null);

        HeaderViewHolder viewHolder = new HeaderViewHolder(view);
        viewHolder.init();

        addHeaderView(view);
        return viewHolder;
    }

    private FooterViewHolder initFooterView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_footer_layout, null);
        FooterViewHolder viewHolder = new FooterViewHolder(view);
        viewHolder.init();
        addFooterView(view);

        return viewHolder;
    }

    private int downY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!onMove(ev)) {
                    break;
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onUp();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean onMove(MotionEvent event) {
        if (refreshState == RefreshState.REFRESHING) {
            return false;
        }

        int deltaY = (int) (event.getY() - downY);
        if (getFirstVisiblePosition() == 0) {
            int initPaddingTop = headerViewHolder.getInitPaddingTop();
            int paddingTop = initPaddingTop + deltaY;
            if (paddingTop <= initPaddingTop) {
                return false;
            }

            headerViewHolder.setPaddingTop(paddingTop);

            if (paddingTop >= 0 && refreshState == RefreshState.PULL_TO_REFRESH) {
                setRefreshState(RefreshState.RELEASE_TO_REFRESH);
            } else if (paddingTop < 0 && refreshState == RefreshState.RELEASE_TO_REFRESH) {
                setRefreshState(RefreshState.PULL_TO_REFRESH);
            }
            return true;
        }
        return false;
    }

    private void onUp() {
        if (refreshState == RefreshState.PULL_TO_REFRESH) {
            headerViewHolder.hide();
        } else if (refreshState == RefreshState.RELEASE_TO_REFRESH) {
            headerViewHolder.show();

            setRefreshState(RefreshState.REFRESHING);

            if (refreshListener != null) {
                refreshListener.onRefresh();
            }
        }
    }

    private void setRefreshState(RefreshState state) {
        refreshState = state;
        refreshHeaderView();
    }

    private void refreshHeaderView() {
        @StringRes int id;
        switch (refreshState) {
            default:
            case PULL_TO_REFRESH:
                id = R.string.pull_to_refresh;
                break;
            case RELEASE_TO_REFRESH:
                id = R.string.release_to_refresh;
                break;
            case REFRESHING:
                id = R.string.refreshing;
                break;
        }
        headerViewHolder.setText(id);
    }

    public void setLoadMoreCount(int count) {
        loadMoreCount = count;
    }

    public void onLoadMoreFinished() {
        isLoadingMore = false;
        isShowingFooter = false;
        footerViewHolder.hide();
    }

    public void onRefreshFinished() {
        headerViewHolder.hide();
        setRefreshState(RefreshState.PULL_TO_REFRESH);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("rango-scroll_changed", "scrollState = " + scrollState + ", lastPosition = " + getLastVisiblePosition() + ", fistPosition = " + getFirstVisiblePosition() + ", count =" + getCount());

        int lastVisiblePosition = getLastVisiblePosition();
        int count = getCount();
        int lastPosition = count - 1;

        if (!isLoadingMore && lastVisiblePosition == lastPosition - loadMoreCount) {
            isLoadingMore = true;

            if (refreshListener != null) {
                refreshListener.onLoadMore();
            }
        }

        if (!isShowingFooter && lastVisiblePosition == count - 1) {
            isShowingFooter = true;
            footerViewHolder.show();
            setSelection(getCount() - 1);

            if (!isLoadingMore) {
                if (refreshListener != null) {
                    refreshListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("rango-onScroll", "firstVisibleItem = " + firstVisibleItem + "visibleItemCount = " + visibleItemCount + ", totalItemCount = " + totalItemCount + ", lastPosition = " + getLastVisiblePosition() + ", fistPosition = " + getFirstVisiblePosition() + ", count =" + getCount());
    }

    public interface RefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    private class HeaderViewHolder {

        private View view;
        private ImageView imageView;
        private TextView textView;
        private int height;

        HeaderViewHolder(View view) {
            this.view = view;
            imageView = view.findViewById(R.id.header_image_view);
            textView = view.findViewById(R.id.header_text_view);
        }

        void init() {
            view.measure(0, 0);
            height = view.getMeasuredHeight();
            hide();
        }

        int getInitPaddingTop() {
            return -height;
        }

        void setPaddingTop(int paddingTop) {
            view.setPadding(0, paddingTop, 0, 0);
        }

        void setText(@StringRes int id) {
            textView.setText(id);
        }

        void show() {
            setPaddingTop(0);
        }

        void hide() {
            setPaddingTop(-height);
        }
    }

    private class FooterViewHolder {

        private View view;
        private TextView textView;
        private int height;

        FooterViewHolder(View view) {
            this.view = view;
            textView = view.findViewById(R.id.footer_text_view);
        }

        void init() {
            view.measure(0, 0);
            height = view.getMeasuredHeight();
            hide();
        }

        void show() {
            view.setPadding(0, 0, 0, 0);
        }

        void hide() {
            view.setPadding(0, -height, 0, 0);
        }
    }

    private enum RefreshState {
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING
    }
}
