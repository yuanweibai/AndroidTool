package rango.tool.androidtool.list.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        findViewById(R.id.image_view).setOnLongClickListener(v -> {
            Toast.makeText(getContext(), "fjakjdfkla", Toast.LENGTH_LONG).show();
            return true;
        });
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
