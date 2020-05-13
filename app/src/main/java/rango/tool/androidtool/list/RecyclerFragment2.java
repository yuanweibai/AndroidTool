package rango.tool.androidtool.list;

import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    private static final String TAG = RecyclerFragment2.class.getSimpleName();


    private RefreshRecyclerView recyclerView;
    private MyRecyclerAdapter adapter;

    private List<BaseItemData> data;

    public void updateData() {
        if (adapter != null) {
            Log.e("rango", "--------------------isComputingLayout = ");

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_2;
    }

    @Override
    protected void initView(View view) {

        recyclerView = view.findViewById(R.id.refresh_recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        addHeaderView();
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ListRecyclerViewDivider());
        adapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(adapter);

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

            }
        });


        data = getTestData();
        adapter.update(data);
    }

    private void addHeaderView() {
        HeaderView headerView = new HeaderView(getContext());
        recyclerView.addHeaderView(headerView);
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add(new BaseItemData("data - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }

        dataList.add(3, new BaseItemData(10, BaseItemType.TYPE_LIST_NEST));
        return dataList;
    }

    private List<BaseItemData> getRefreshData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(new BaseItemData("refresh - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }

    private static class ListRecyclerViewDivider extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int count = parent.getAdapter().getItemCount();
            int lastPosition = count - 1;
            if (position == lastPosition) {
                return;
            }
            outRect.bottom = ScreenUtils.dp2px(8);
        }
    }


}
