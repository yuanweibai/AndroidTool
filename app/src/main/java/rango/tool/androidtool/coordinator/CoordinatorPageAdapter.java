package rango.tool.androidtool.coordinator;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CoordinatorPageAdapter extends FragmentPagerAdapter {


    private List<TitleFragment> mFragmentList;

    public CoordinatorPageAdapter(FragmentManager fm, List<TitleFragment> fragmentList) {
        super(fm);

        mFragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
