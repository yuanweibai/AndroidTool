package rango.tool.androidtool.list;

import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseFragment;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.base.list.view.SpecificRecyclerVIew;
import rango.tool.androidtool.list.view.ListBannerView;
import rango.tool.common.utils.ScreenUtils;
import rango.tool.common.utils.Worker;

public class RecyclerFragment2 extends BaseFragment {

    private RefreshRecyclerView recyclerView;
    private MyRecyclerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_2;
    }

    @Override
    protected void initView(View view) {

        recyclerView = view.findViewById(R.id.refresh_recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);


        addHeaderView();
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ListRecyclerViewDivider());
        adapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setPositionBackToLoadMore(3);

        recyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {
                        adapter.update(getRefreshData());
                        recyclerView.scrollToPosition(0);
                        recyclerView.onRefreshed();
                    }
                }, 5000);
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(getContext(), "load more.......", Toast.LENGTH_SHORT).show();
                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.onLoadMoreFinished();

                        adapter.append(getLoadMoreData());

                    }
                }, 5000);
            }
        });

        adapter.update(getTestData());
    }

    private void addHeaderView() {
        HeaderView headerView = new HeaderView(getContext());
        recyclerView.addHeaderView(headerView);
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new BaseItemData("data - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }

    private List<BaseItemData> getRefreshData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new BaseItemData("refresh - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }

    private List<BaseItemData> getLoadMoreData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new BaseItemData("loadMore - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }

    private static class ListRecyclerViewDivider extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
            if (holder.getItemViewType() == BaseItemType.TYPE_LIST_FOOTER) {
                return;
            }
            outRect.bottom = ScreenUtils.dp2px(8);
        }
    }


}
