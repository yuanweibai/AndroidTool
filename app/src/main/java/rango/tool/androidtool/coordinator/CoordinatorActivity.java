package rango.tool.androidtool.coordinator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class CoordinatorActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorPageAdapter coordinatorPageAdapter;
    private List<TitleFragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);
        initView();
    }

    private void initView() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);


        addTestData();
        coordinatorPageAdapter = new CoordinatorPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(coordinatorPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        for (int i = 0;i<tabLayout.getTabCount();i++){
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null){
//                tab.setIcon(R.drawable.coin_icon);
//            }
//        }

    }

    private void addTestData() {
        if (fragments != null) {
            fragments.clear();
        } else {
            fragments = new ArrayList<>();
        }

        for (int i = 0; i < 8; i++) {
            fragments.add(ListFragment.newInstance("coordinator" + i));
        }
    }
}
