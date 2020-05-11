package rango.tool.androidtool.list;

import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
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
import rango.tool.common.utils.ScreenUtils;
import rango.tool.common.utils.Worker;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class RecyclerFragment extends BaseFragment {


    private static final String TAG = RecyclerFragment.class.getSimpleName();

    private SpecificRecyclerVIew recyclerView;
    private MyRecyclerAdapter adapter;
    private ListBannerView listBannerView;
    private List<BaseItemData> data;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void updateData() {
        if (adapter != null) {
            Log.e("rango", "--------------------isComputingLayout = " + recyclerView.isComputingLayout());
            recyclerView.requestLayout();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        //1. 设置圈圈的偏移量，从哪儿移动到哪儿，是否需要缩放
        swipeRefreshLayout.setProgressViewOffset(true, ScreenUtils.dp2px(10), ScreenUtils.dp2px(60));

        //2. 设置开始刷新的偏移量
        swipeRefreshLayout.setDistanceToTriggerSync(ScreenUtils.dp2px(30));

        //3. 设置圈圈的大小，有两种：LARGE 和 DEFAULT
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        //4. 设置圈圈的颜色，可以循环变换，个数似乎是没有限制，请求很快的话，设置太多的颜色是没有必要的
        swipeRefreshLayout.setColorSchemeResources(
                R.color.red,
                R.color.blue,
                R.color.gray,
                R.color.yellow,
                R.color.green
        );

        //5. 设置圈圈的背景颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.black);

        //6. 监听刷新动作
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("rango", "refresh ....");

                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {

                        //7. 取消刷新圈圈
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },5000);
            }
        });


        listBannerView = view.findViewById(R.id.banner_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                int position = holder.getAdapterPosition();
                Log.e("rango", "RecyclerView: recycler, position = " + position);
            }
        });
//        recyclerView.setRealView(listBannerView);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                if (e.getAction() != MotionEvent.ACTION_UP) {
                    return;
                }

                View child = rv.findChildViewUnder(e.getX(), e.getY());

                RecyclerView.ViewHolder holder = rv.getChildViewHolder(child);

                int position = holder.getAdapterPosition();

                //todo 处理点击事件
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ListRecyclerViewDivider());
        adapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.e("rango", "newState = " + newState + ", isComputingLayout = " + recyclerView.isComputingLayout());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                listBannerView.scrollBy(dx, dy);

//                if (listBannerView.getScrollY() < listBannerView.getHeight()) {
//                    int y = dy;
//                    if (listBannerView.getScrollY() + dy > listBannerView.getHeight()) {
//                        y = listBannerView.getScrollY() + dy - listBannerView.getHeight();
//                    }
//                    Log.e(TAG, "listBannerView.scrollY = " + listBannerView.getScrollY() + ", listBannerView.height = " + listBannerView.getHeight());
//                    listBannerView.scrollBy(dx, y);
//                } else if (listBannerView.getScrollY() > listBannerView.getHeight()) {
//
//                    int y = listBannerView.getScrollY() - listBannerView.getHeight();
//                    Log.e(TAG, "listBannerView.scrollY = " + listBannerView.getScrollY() + ", listBannerView.height = " + listBannerView.getHeight());
//                    listBannerView.scrollBy(dx, -y);
//                }
            }
        });

        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                Log.e("rango", "Fling......, isComputingLayout = " + recyclerView.isComputingLayout());
                return false;
            }
        });
        data = getTestData();
        adapter.append(data);
    }

    private List<BaseItemData> getTestData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add(new BaseItemData("data - " + i, BaseItemType.TYPE_LIST_NORMAL));
        }

        dataList.add(3, new BaseItemData(10, BaseItemType.TYPE_LIST_NEST));
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
