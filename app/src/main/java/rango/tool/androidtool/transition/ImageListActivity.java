package rango.tool.androidtool.transition;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.data.DataProvider;
import rango.tool.common.utils.ScreenUtils;


public class ImageListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ImageListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        recyclerView = findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new Divider());
        adapter = new ImageListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.update(DataProvider.getImageListActivityData());
    }

    private static class Divider extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int space = ScreenUtils.dp2px(12);
            outRect.top = space;
            outRect.bottom = space;
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            int spanCount = ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount();

            if (spanIndex == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            } else if (spanIndex == spanCount - 1) {
                outRect.left = space / 2;
                outRect.right = space;
            } else {
                outRect.left = space / 2;
                outRect.right = space / 2;
            }
        }
    }
}
