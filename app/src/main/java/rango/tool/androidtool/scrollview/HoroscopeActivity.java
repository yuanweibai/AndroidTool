package rango.tool.androidtool.scrollview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class HoroscopeActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HoroscopeLayout mRootView;
    private HoroscopePageAdapter mHoroscopePageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_layout);
        initView();
    }

    private void initView() {
        mRootView = findViewById(R.id.root_view);
        mTabLayout = findViewById(R.id.horoscope_tabs);
        mRootView.setTopView(mTabLayout);
        mViewPager = findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TodayFragment());
        fragments.add(new TomorrowFragment());
        mHoroscopePageAdapter = new HoroscopePageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mHoroscopePageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

}
