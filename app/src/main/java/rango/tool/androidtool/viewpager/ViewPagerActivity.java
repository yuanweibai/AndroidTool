package rango.tool.androidtool.viewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.coordinator.ListFragment;
import rango.tool.androidtool.viewpager.transformpage.ParallaxTransformer;
import rango.tool.common.utils.ScreenUtils;

public class ViewPagerActivity extends BaseActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getTestView());
//        viewPager.setPageMargin(ScreenUtils.dp2px(18));
        viewPager.setOffscreenPageLimit(2);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getTestFragment());
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(false, new ParallaxTransformer());

//        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                String tag = (String) page.getTag();
//                float offset = viewPager.getPageMargin() / (float) page.getWidth();
//                Log.e("rango", "tag = " + tag + ", position = " + position + ", offset = " + offset);
//
////                if (position < -1) {
////                    page.setAlpha(0f);
////                } else if (position <= 1) {
////                    if (position < 0) {
////                        page.setAlpha(1 + position);
////                    } else {
////                        page.setAlpha(1 - position);
////                    }
////                } else {
////                    page.setAlpha(0f);
////                }
//            }
//        });
    }

    private List<View> getTestView() {
        List<View> result = new ArrayList<>();
        int[] colorArray = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
        int[] resIdArray = new int[]{R.drawable.pig_farm_bg,R.drawable.screen_bg,R.drawable.solon};
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_test_1, null);
            view.setBackgroundColor(colorArray[random.nextInt(colorArray.length)]);
            TextView msgText = view.findViewById(R.id.msg_text);
            msgText.setText(String.valueOf(i));

            ImageView imageView = view.findViewById(R.id.image_view);
            imageView.setImageResource(resIdArray[new Random().nextInt(resIdArray.length)]);
            view.setTag(String.valueOf(i));

            result.add(view);
        }

        return result;
    }

    private List<Fragment> getTestFragment() {
        List<Fragment> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            result.add(ListFragment.newInstance(String.valueOf(i)));
        }
        return result;
    }
}
