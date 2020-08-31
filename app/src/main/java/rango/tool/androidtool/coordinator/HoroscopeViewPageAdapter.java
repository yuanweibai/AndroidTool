package rango.tool.androidtool.coordinator;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;

public class HoroscopeViewPageAdapter extends PagerAdapter {

    static int[] TITLE_IDS = new int[]{
            R.string.today,
            R.string.tomorrow
    };

    private Context mContext;

    public HoroscopeViewPageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ToolApplication.getContext().getString(TITLE_IDS[position]);
    }

    @Override
    public int getCount() {
        return TITLE_IDS.length;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int positionAbsolute) {
//        if (positionAbsolute == (sIsRtl ? 2 : 0)) {
//            if (mHotTabContent == null) {
//                mHotTabContent = createTabHotContent(container);
//            }
//            container.addView(mHotTabContent, 0);
//            return mHotTabContent;
//        } else if (positionAbsolute == 1) {
//            if (mAllTabContent == null) {
//                mAllTabContent = createTabContent(NewAndCategoryThemeAdapter.THEME_PAGE_NEW, container);
//            }
//            container.addView(mAllTabContent, 0);
//            return mAllTabContent;
//        } else {
//            if (mCategoryTabContent == null) {
//                mCategoryTabContent = createCategoryTabContent(container);
//            }
//            container.addView(mCategoryTabContent, 0);
//            return mCategoryTabContent;
//        }
        View view;
        if (positionAbsolute == 0) {
            view = createTodayView(container);
        } else {
            view = createTomorrowView(container);
        }
        container.addView(view, 0);
        return view;
    }

    private View createTodayView(ViewGroup container) {
        Context context = mContext;
        View contentView = LayoutInflater.from(context).inflate(R.layout.today_layout, container, false);
        return contentView;
    }

    private View createTomorrowView(ViewGroup container) {
        Context context = mContext;
        View contentView = LayoutInflater.from(context).inflate(R.layout.tomorrow_layout, container, false);
        return contentView;
    }

}
