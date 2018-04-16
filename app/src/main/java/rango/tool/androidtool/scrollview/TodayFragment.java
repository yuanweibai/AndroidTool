package rango.tool.androidtool.scrollview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseFragment;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.list.MyRecyclerAdapter;

public class TodayFragment extends BaseFragment {

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
        return manager.findFirstCompletelyVisibleItemPosition() == 0;
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add(new BaseItemData("today - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }

}
