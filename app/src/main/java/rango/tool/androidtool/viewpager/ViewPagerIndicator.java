package rango.tool.androidtool.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerIndicator extends LinearLayout {

    private List<String> mTabs = new ArrayList<>();
    private List<AppCompatTextView> mTabTextViews = new ArrayList<>();

    private Context mContext;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        mContext = context;
    }

    public void setTabs(String... tabs) {
        mTabs.clear();
        mTabTextViews.clear();
        removeAllViews();
        mTabs.addAll(Arrays.asList(tabs));

        resetTabs();
    }

    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onTabSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onTabSelect(int position) {
        for (int i = 0; i < mTabTextViews.size(); i++) {
            if (i == position) {
                mTabTextViews.get(i).setTextColor(Color.RED);
            } else {
                mTabTextViews.get(i).setTextColor(Color.BLACK);
            }
        }
    }

    public void resetTabs() {
        for (String tab : mTabs) {
            AppCompatTextView textView = getTextView(tab);
            mTabTextViews.add(textView);
            addView(textView, getTabParams());
        }
    }

    public AppCompatTextView getTextView(String tab) {
        AppCompatTextView textView = new AppCompatTextView(mContext);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        textView.setText(tab);
        return textView;
    }

    public LayoutParams getTabParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return params;
    }

}
