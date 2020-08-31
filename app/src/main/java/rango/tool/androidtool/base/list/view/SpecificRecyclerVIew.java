package rango.tool.androidtool.base.list.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * recyclerView 的itemView离开屏幕后会调用view onDetachedFromWindow()方法，
 * 这就导致再次显示时比较慢，尤其是TextureView加载视频时，会有从无到有的一个过程；
 * 所有此SpecificRecyclerView就是为了解决此问题。
 * <p>
 * 解决方法：请看fragment_recycler.xml布局文件；
 */

public class SpecificRecyclerVIew extends RecyclerView {

    private static final String TAG = SpecificRecyclerVIew.class.getSimpleName();

    private View realView;

    public SpecificRecyclerVIew(Context context) {
        this(context, null);
    }

    public SpecificRecyclerVIew(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpecificRecyclerVIew(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void setRealView(View view) {
        this.realView = view;
    }

    private void initView() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollRealView(dx, dy);
            }
        });
    }

    private void scrollRealView(int dx, int dy) {
        if (realView == null) {
            return;
        }
        realView.scrollBy(dx, dy);

//        int height = realView.getHeight();
//        int scrollY = realView.getScrollY();
//        Log.e(TAG, "height = " + height + ", scrollY = " + scrollY + ", dy = " + dy);
//
//        if (scrollY <= height) {
//            int y = dy;
//            if (scrollY + dy > height) {
//                y = height - scrollY;
//            } else if (scrollY + dy < 0) {
//                y = 0 - scrollY;
//            }
//            realView.scrollBy(dx, y);
//        }
    }


}
