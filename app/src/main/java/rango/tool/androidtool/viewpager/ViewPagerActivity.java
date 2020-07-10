package rango.tool.androidtool.viewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ViewPagerActivity extends BaseActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new ViewPagerAdapter(getTestView());
        viewPager.setAdapter(adapter);
    }

    private List<View> getTestView() {
        List<View> result = new ArrayList<>();
        int[] colorArray = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_test_1, null);
            view.setBackgroundColor(colorArray[random.nextInt(colorArray.length)]);
            TextView msgText = view.findViewById(R.id.msg_text);
            msgText.setText(String.valueOf(i));

            result.add(view);
        }

        return result;
    }
}
