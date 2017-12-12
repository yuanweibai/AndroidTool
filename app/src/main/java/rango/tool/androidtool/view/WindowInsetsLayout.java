package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import rango.tool.androidtool.view.util.InsetListener;
import rango.tool.common.utils.WindowUtil;

public class WindowInsetsLayout extends RelativeLayout
        implements ViewGroup.OnHierarchyChangeListener, InsetListener {

    private static final int NAVIGATION_BAR_STATUS_INIT = -1;
    private static final int NAVIGATION_BAR_STATUS_SHOW = 0;
    private static final int NAVIGATION_BAR_STATUS_HIDE = 1;

    private int mNavigationBarStatus = NAVIGATION_BAR_STATUS_INIT;
    private Rect insetRect = new Rect();
    private NavigationBarStatusListener navigationBarStatusListener;

    public WindowInsetsLayout(Context context) {
        this(context, null);
    }

    public WindowInsetsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WindowInsetsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnHierarchyChangeListener(this);
    }

    public void setNavigationBarStatusListener(NavigationBarStatusListener listener) {
        navigationBarStatusListener = listener;
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        updateNavigationBarStatus(insets.bottom);
        setInsets(insets);
        return true;
    }

    private void updateNavigationBarStatus(int bottom) {
        int navigationBarHeight = WindowUtil.getNavigationBarHeight(getContext());
        if ((mNavigationBarStatus != NAVIGATION_BAR_STATUS_SHOW)
                && bottom > 0
                && bottom == navigationBarHeight) {
            mNavigationBarStatus = NAVIGATION_BAR_STATUS_SHOW;
            if (navigationBarStatusListener != null) {
                navigationBarStatusListener.onShow();
            }
        } else if ((mNavigationBarStatus != NAVIGATION_BAR_STATUS_HIDE)
                && navigationBarHeight > 0
                && bottom == 0) {
            mNavigationBarStatus = NAVIGATION_BAR_STATUS_HIDE;
            if (navigationBarStatusListener != null) {
                navigationBarStatusListener.onHide();
            }
        }
    }

    @Override
    public void setInsets(Rect insets) {

    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {

    }

    interface NavigationBarStatusListener {
        void onShow();

        void onHide();
    }
}
