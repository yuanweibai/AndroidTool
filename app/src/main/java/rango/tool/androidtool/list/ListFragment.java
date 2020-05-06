package rango.tool.androidtool.list;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.LifecycleFragment;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.list.view.ListBannerView;
import rango.tool.common.utils.Worker;

public class ListFragment extends LifecycleFragment {

    static {
        TAG = "RangoFragment";
    }

    private CustomListView listView;
    private MyListAdapter adapter;
    private ListBannerView listBannerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        listView = view.findViewById(R.id.list_view);
        listBannerView = new ListBannerView(getContext());
        adapter = new MyListAdapter(getContext(), getTestData(), listBannerView);
        listView.setAdapter(adapter);
        listView.setLoadMoreCount(3);

        listView.setRefreshListener(new CustomListView.RefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("rango", "refresh.........");
                Worker.postMain(() -> {
                    adapter.update(getRefreshData());
                    listView.onRefreshFinished();
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                Log.e("rango", "load more.........");

                Worker.postMain(() -> {
                    adapter.append(getLoadMoreData());
                    listView.onLoadMoreFinished();
                }, 3000);
            }
        });
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        dataList.add(new BaseItemData(1, BaseItemType.TYPE_LIST_BANNER));
        for (int i = 0; i < 50; i++) {
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
}
