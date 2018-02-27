package rango.tool.androidtool.scrollview;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;

public class HoroscopePageAdapter extends FragmentPagerAdapter {
    static int[] TITLE_IDS = new int[]{
            R.string.today,
            R.string.tomorrow
    };

    private List<Fragment> mFragmentList;

    public HoroscopePageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);

        mFragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return TITLE_IDS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ToolApplication.getContext().getString(TITLE_IDS[position]);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
