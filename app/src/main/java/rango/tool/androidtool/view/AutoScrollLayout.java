package rango.tool.androidtool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.R;

public class AutoScrollLayout extends AutoScrollCircleLayout<String> {
    private static final String TAG = AutoScrollLayout.class.getSimpleName();
    private List<String> dataList;

    public AutoScrollLayout(Context context) {
        this(context, null);
    }

    public AutoScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected List<View> getItemData(List<String> data) {
        dataList = data;
        List<View> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ImageView view = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.auto_scroll_item_view, null);
            final String pic = dataList.get(i);
            GlideApp.with(this)
                    .load(pic)
                    .placeholder(R.drawable.load_image_default)
                    .error(R.drawable.load_image_error)
                    .into(view);
            list.add(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewClick(pic);
                }
            });
        }
        return list;
    }

    private void itemViewClick(String pic) {
        Log.e(TAG, "pic = " + pic);
    }
}
