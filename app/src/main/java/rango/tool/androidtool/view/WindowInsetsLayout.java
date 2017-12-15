package rango.tool.androidtool.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import rango.tool.androidtool.R;
import rango.tool.androidtool.view.util.InsetsListener;

/**
 * 虚拟按键
 * `Android` 是在 `API 19` 的时候开始支持设置状态栏及虚拟按键透明的。
 * <p>
 * (1) 沉浸式模式（背景延伸到状态栏及虚拟按键下）：
 * --- API >= 21
 * 通过{@link rango.tool.common.utils.WindowUtil#immersiveStatusAndNavigationBar(Activity)} 方法设置。
 * <p>
 * --- 19 <= API < 21
 * 通过在`Activity`的`Style`中添加如下两个属性：
 * <item name="android:windowTranslucentNavigation">true</item>
 * <item name="android:windowTranslucentStatus">true</item>
 * <p>
 * --- 备注
 * 1. 如果是一个`Activity`，则除上述方法外，还需要对其布局的根`View`设置：
 * {@link View#setSystemUiVisibility(int)}(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
 * 如果是一个`Dialog`，可以通过{@link rango.tool.common.utils.WindowUtil#immersiveStatusAndNavigationBar(Window)}方法设置;
 * <p>
 * (2) 普通模式不需要做任何处理，系统提供什么样的虚拟按键就展示什么样的虚拟按键。
 * <p>
 * 用到沉浸式模式，会导致一些问题，如其附属`View`需要对其子`View`整体上移等不同处理，因此需要用到`WindowInsetsLayout`这个类。
 * WindowInsetsLayout 的处理方式：
 * <p>
 * 1. 实现 InsetsListener接口，在其 setInsets() 方法中处理；这种一般是要对其子 View 设置 padding 、margin 或者改变其高度;
 * 2. 设置{@link LayoutParams#mInsetWay}属性(可以动态设置，也可以在 xml 中设置)，默认是 MARGIN ：
 * ------ MARGIN: 通过设置 topMargin 和 bottomMargin 来改变其子 View 的位置;
 * ------ PADDING: 通过设置 paddingTop 和 paddingBottom 改变其子 View 的位置;
 * ------ NONE: 不做任何处理;
 * <p>
 * 注意事项
 * 1. 如果 View 需要做类似于从底部升起的动画时，此 View 不能相对于底部布局，否则在虚拟按键可伸缩的机子上，
 * 在动画的过程中伸缩虚拟按键就会出现布局问题。解决方案就是相对于顶部布局，设置其 topMargin。
 */
public class WindowInsetsLayout extends RelativeLayout
        implements ViewGroup.OnHierarchyChangeListener, InsetsListener {

    protected Rect mInsets = new Rect();
    private boolean mScreenLocked = false;

    public WindowInsetsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnHierarchyChangeListener(this);
    }

    public void setFrameLayoutChildInsets(View child, Rect newInsets, Rect oldInsets) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        if (child instanceof InsetsListener) {
            ((InsetsListener) child).setInsets(newInsets);
        } else if (lp.mInsetWay == LayoutParams.InsetWay.MARGIN) {
            lp.topMargin += (newInsets.top - oldInsets.top);
            lp.leftMargin += (newInsets.left - oldInsets.left);
            lp.rightMargin += (newInsets.right - oldInsets.right);
            lp.bottomMargin += (newInsets.bottom - oldInsets.bottom);
        } else if (lp.mInsetWay == LayoutParams.InsetWay.PADDING) {
            child.setPadding(child.getPaddingLeft(),
                    child.getPaddingTop() + (newInsets.top - oldInsets.top),
                    child.getPaddingRight(),
                    child.getPaddingBottom() + (newInsets.bottom - oldInsets.bottom));
        } else if (lp.mInsetWay == LayoutParams.InsetWay.NONE) {
            //nothing
        } else {
            //nothing
        }
    }

    @Override
    public void setInsets(Rect insets) {
        if (mScreenLocked) {
            return;
        }
        final int n = getChildCount();
        for (int i = 0; i < n; i++) {
            final View child = getChildAt(i);
            setFrameLayoutChildInsets(child, insets, mInsets);
        }
        mInsets.set(insets);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WindowInsetsLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof WindowInsetsLayout.LayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        setFrameLayoutChildInsets(child, mInsets, new Rect());
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

        private static final int DEFAULT_WAY = 2;
        public InsetWay mInsetWay;

        @SuppressLint("CustomViewStyleable")
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.InsetAttr);
            int value = a.getInt(R.styleable.InsetAttr_layout_insetWay, DEFAULT_WAY);
            a.recycle();
            mInsetWay = getInsetWay(value);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams lp) {
            super(lp);
        }

        private InsetWay getInsetWay(int value) {
            switch (value) {
                case 0:
                    return InsetWay.NONE;
                case 1:
                    return InsetWay.PADDING;
                case 2:
                default:
                    return InsetWay.MARGIN;
            }
        }

        public enum InsetWay {
            NONE,
            MARGIN,
            PADDING
        }
    }

}
