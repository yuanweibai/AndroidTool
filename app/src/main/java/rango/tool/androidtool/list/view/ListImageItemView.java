package rango.tool.androidtool.list.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemView;

public class ListImageItemView extends BaseItemView {

    private TextView textView;

    private String msg;

    public ListImageItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.list_image_item_layout, this);

        textView = findViewById(R.id.msg_text);
    }

    @Override
    protected void refreshUI() {
        if (mData == null || mData.getData() == null) {
            return;
        }

        msg = (String) mData.getData();
        textView.setText(msg);
    }
}
