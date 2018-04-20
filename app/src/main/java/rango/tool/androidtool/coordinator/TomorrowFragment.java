package rango.tool.androidtool.coordinator;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseFragment;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.list.MyRecyclerAdapter;

public class TomorrowFragment extends BaseFragment {

    private MyRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager manager;

    @Override
    protected int getLayoutId() {
        return R.layout.recycler_view;
    }

    @Override protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);

        manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(adapter);

        adapter.update(getTestData());
    }

    public boolean isTop() {
        int y = mRecyclerView.getScrollY();
        Log.e("rango", "isTop() y = " + y);
        return true;
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add(new BaseItemData("tomorrow - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }
}
