package rango.tool.androidtool.list;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseFragment;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.base.list.view.SpecificRecyclerVIew;
import rango.tool.androidtool.list.view.ListBannerView;
import rango.tool.androidtool.list.view.ListImageItemView;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class RecyclerFragment extends BaseFragment {


    private static final String TAG = RecyclerFragment.class.getSimpleName();

    private SpecificRecyclerVIew recyclerView;
    private MyRecyclerAdapter adapter;
    private ListBannerView listBannerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initView(View view) {
        listBannerView = view.findViewById(R.id.banner_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setRealView(listBannerView);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ListRecyclerViewDivider());
        adapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(adapter);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                listBannerView.scrollBy(dx, dy);
//
////                if (listBannerView.getScrollY() < listBannerView.getHeight()) {
////                    int y = dy;
////                    if (listBannerView.getScrollY() + dy > listBannerView.getHeight()) {
////                        y = listBannerView.getScrollY() + dy - listBannerView.getHeight();
////                    }
////                    Log.e(TAG, "listBannerView.scrollY = " + listBannerView.getScrollY() + ", listBannerView.height = " + listBannerView.getHeight());
////                    listBannerView.scrollBy(dx, y);
////                } else if (listBannerView.getScrollY() > listBannerView.getHeight()) {
////
////                    int y = listBannerView.getScrollY() - listBannerView.getHeight();
////                    Log.e(TAG, "listBannerView.scrollY = " + listBannerView.getScrollY() + ", listBannerView.height = " + listBannerView.getHeight());
////                    listBannerView.scrollBy(dx, -y);
////                }
//            }
//        });
        adapter.append(getTestData());
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
//        dataList.add(new BaseItemData(ScreenUtils.dp2px(100), BaseItemType.TYPE_LIST_EMPTY));
        for (int i = 0; i < 20; i++) {
            dataList.add(new BaseItemData("data - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        dataList.add(new BaseItemData("", BaseItemType.TYPE_LIST_IMAGE));
        for (int i = 20; i < 50; i++) {
            dataList.add(new BaseItemData("data - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }
        return dataList;
    }

    private static class ListRecyclerViewDivider extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = 0;
            outRect.bottom = 0;
            if (view instanceof ListImageItemView) {
                outRect.bottom = 100;
                outRect.top = 20;
            }

        }
    }


}
