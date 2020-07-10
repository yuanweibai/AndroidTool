package rango.tool.androidtool.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
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
        Log.e("rango","container = "+container+", position = "+position);
        View view = itemViewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e("rango-destroyItem","container = "+container+", position = "+position+", object = "+object);
        container.removeView(itemViewList.get(position));
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        Log.e("rango-setPrimaryItem","container = "+container+", position = "+position+", object = "+object);
    }

    public void update(List<View> viewList) {
        itemViewList.clear();
        itemViewList.addAll(viewList);
        notifyDataSetChanged();
    }
}
