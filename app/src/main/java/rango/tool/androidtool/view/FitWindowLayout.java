package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import rango.tool.common.utils.ScreenUtils;

/**
 * 主要用于监听虚拟按键的显示与隐藏
 */
public class FitWindowLayout extends WindowInsetsLayout {

    private static final int NAVIGATION_BAR_STATUS_INIT = -1;
    private static final int NAVIGATION_BAR_STATUS_SHOW = 0;
    private static final int NAVIGATION_BAR_STATUS_HIDE = 1;
    private NavigationBarStatusListener mNavigationBarListener;
    private int mNavigationBarStatus = NAVIGATION_BAR_STATUS_INIT;

    public FitWindowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        updateNavigationBarStatus(insets);
        Rect resultInsets = new Rect(
                insets.left,
                ScreenUtils.getStatusBarHeight(),
                insets.right,
                insets.bottom);
        setInsets(resultInsets);
        return true;
    }

    public void setOnNavigationStatusListener(NavigationBarStatusListener listener) {
        mNavigationBarListener = listener;
    }

    private void updateNavigationBarStatus(Rect insets) {
        int navigationBarHeight = ScreenUtils.getNavigationBarHeight(getContext());
        if ((mNavigationBarStatus != NAVIGATION_BAR_STATUS_SHOW)
                && insets.bottom > 0
                && insets.bottom == navigationBarHeight) {
            mNavigationBarStatus = NAVIGATION_BAR_STATUS_SHOW;
            if (mNavigationBarListener != null) {
                mNavigationBarListener.onShow();
            }
        } else if ((mNavigationBarStatus != NAVIGATION_BAR_STATUS_HIDE)
                && navigationBarHeight > 0
                && insets.bottom == 0) {
            mNavigationBarStatus = NAVIGATION_BAR_STATUS_HIDE;
            if (mNavigationBarListener != null) {
                mNavigationBarListener.onHide();
            }
        }
    }

    interface NavigationBarStatusListener {
        void onShow();

        void onHide();
    }
}
