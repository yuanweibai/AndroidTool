package rango.tool.androidtool.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> itemViewList;

    public ViewPagerAdapter(List<View> itemViewList) {
        this.itemViewList = itemViewList;
    }

    @Override
    public int getCount() {
        return itemViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = itemViewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(itemViewList.get(position));
    }

    public void update(List<View> viewList) {
        itemViewList.clear();
        itemViewList.addAll(viewList);
        notifyDataSetChanged();
    }
}
