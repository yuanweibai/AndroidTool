package rango.tool.androidtool.list.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemView;

public class ListNormalIteView extends BaseItemView {

    private static final String TAG = ListNormalIteView.class.getSimpleName();

    private TextView textView;

    private String data;

    public ListNormalIteView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.list_normal_item_view, this);
        initView();
    }

    private void initView() {
        textView = findViewById(R.id.text);
    }

    @Override
    protected void refreshUI() {
        if (mData == null || mData.getData() == null) {
            return;
        }
        data = String.valueOf(mData.getData());
        textView.setText(data);
    }
}
