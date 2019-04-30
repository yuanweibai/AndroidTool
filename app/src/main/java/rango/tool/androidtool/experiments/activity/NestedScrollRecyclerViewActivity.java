package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.list.MyRecyclerAdapter;

public class NestedScrollRecyclerViewActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private View scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_layout);

        recyclerView = findViewById(R.id.recycler_view);
        scrollView = findViewById(R.id.scroll_view);

        findViewById(R.id.show_btn).setOnClickListener(v -> scrollView.setVisibility(View.VISIBLE));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        MyRecyclerAdapter adapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.update(getTestData());
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add(new BaseItemData("hhhh" + "- " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }


    @Override
    public void onBackPressed() {
        if (scrollView.getVisibility() == View.VISIBLE) {
            scrollView.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
