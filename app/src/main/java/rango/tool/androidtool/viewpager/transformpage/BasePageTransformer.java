package rango.tool.androidtool.viewpager.transformpage;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public abstract class BasePageTransformer implements ViewPager.PageTransformer {

    private final static float INVALID_VALUE = -1f;
    private float offset = INVALID_VALUE;

    private int pageMargin;

    public BasePageTransformer(int pageMargin) {
        this.pageMargin = pageMargin;
    }

    public abstract void onPageTransform(View page, float process);

    @Override
    public void transformPage(@NonNull View page, float position) {

        if (position < -2 - 2 * offset) {
            return;
        }

        if (position > 2 + 2 * offset) {
            return;
        }

        if (offset == INVALID_VALUE) {
            offset = pageMargin / (float) page.getWidth();
        }

        onPageTransform(page, position / (1 + offset));
    }
}
