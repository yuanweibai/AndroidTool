package rango.tool.androidtool.list.view;

import android.content.Context;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.base.list.adapter.BaseItemView;
import rango.tool.androidtool.list.MyRecyclerAdapter;

public class ListNestItemView extends BaseItemView {

    private RecyclerView recyclerView;
    private MyRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager linearLayoutManager;

    private Parcelable parcelable;

    private int size;

    public ListNestItemView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.list_nest_item_layout, this);

        initView();
    }

    private void initView() {

        recyclerView = findViewById(R.id.nest_recycler_view);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    protected void refreshUI() {

        if (mData == null || mData.getData() == null) {
            return;
        }

        size = (int) mData.getData();

        recyclerAdapter.update(getTestData(size));

        restoreInstanceState();
    }

    private List<BaseItemData> getTestData(int size) {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataList.add(new BaseItemData("nest - " + i, BaseItemType.TYPE_LIST_IMAGE));
        }
        return dataList;
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
    }

    public void saveInstanceState() {
        parcelable = linearLayoutManager.onSaveInstanceState();
    }

    private void restoreInstanceState() {
        if (parcelable != null) {
            linearLayoutManager.onRestoreInstanceState(parcelable);
        }
    }

}
