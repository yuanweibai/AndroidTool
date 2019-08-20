package rango.tool.androidtool.view.util;

import android.content.Context;
import android.widget.Scroller;

public class BannerScroller extends Scroller {
    private static final int DEFAULT_DURATION = 800;
    private int mDuration = DEFAULT_DURATION;

    public BannerScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }
}
