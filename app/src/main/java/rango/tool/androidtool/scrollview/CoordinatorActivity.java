package rango.tool.androidtool.scrollview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class CoordinatorActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HoroscopePageAdapter mHoroscopePageAdapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_layout);
        initView();
    }

    private void initView() {
//        mTabLayout = findViewById(R.id.horoscope_tabs);
//        mViewPager = findViewById(R.id.view_pager);
//        fragments = new ArrayList<>();
//        fragments.add(new TodayFragment());
//        fragments.add(new TomorrowFragment());
//        mHoroscopePageAdapter = new HoroscopePageAdapter(getSupportFragmentManager(), fragments);
//        mViewPager.setAdapter(mHoroscopePageAdapter);
//
//        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }
}
