package rango.tool.androidtool.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import rango.tool.androidtool.R;

public class RefreshRecyclerView extends LinearLayout {

    private ConstraintLayout headerView;
    private ConstraintLayout footerView;
    private RecyclerView recyclerView;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.refresh_recycler_view_layout, this);

        initView();
    }

    private void initView() {
        headerView = findViewById(R.id.header_view);
        footerView = findViewById(R.id.footer_view);
        recyclerView = findViewById(R.id.recycler_view);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (recyclerView != null) {
            recyclerView.addItemDecoration(decoration);
        }
    }

    public void addHeaderView(View view) {
        if (headerView != null) {
            headerView.addView(view);
        }
    }

    public void addFooterView(View view){
        if (footerView != null){
            footerView.addView(view);
        }
    }
}
