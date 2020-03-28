package rango.tool.androidtool.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseFragment;
import rango.tool.androidtool.base.LifecycleActivity;
import rango.tool.androidtool.base.LifecycleFragment;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.list.view.ListBannerView;

public class ListFragment extends LifecycleFragment {

    static {
        TAG = "RangoFragment";
    }

    private ListView listView;
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
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        dataList.add(new BaseItemData(1, BaseItemType.TYPE_LIST_BANNER));
        for (int i = 0; i < 50; i++) {
            dataList.add(new BaseItemData("data - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }
}
