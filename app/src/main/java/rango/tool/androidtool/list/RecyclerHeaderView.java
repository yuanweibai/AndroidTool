package rango.tool.androidtool.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RecyclerHeaderView extends FrameLayout {

    private HeaderView headerView;

    public RecyclerHeaderView(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderView(HeaderView headerView) {
        this.headerView = headerView;
    }

    public void onPullToRefresh() {
        if (headerView != null) {
            headerView.setMsgText("下拉刷新");
        }
    }

    public void onReleaseToRefresh() {
        if (headerView != null) {
            headerView.setMsgText("释放刷新");
        }
    }

    public void onRefreshing() {
        if (headerView != null) {
            headerView.setMsgText("正在刷新");
        }
    }

    public void onRefreshed() {
        if (headerView != null) {
            headerView.setMsgText("刷新完成");
        }
    }
}
