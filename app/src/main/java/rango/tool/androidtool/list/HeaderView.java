package rango.tool.androidtool.list;

import android.content.Context;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import rango.tool.androidtool.R;

public class HeaderView extends ConstraintLayout {

    private TextView msgText;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(Color.RED);
        LayoutInflater.from(context).inflate(R.layout.header_layout, this);

        initView();
    }

    private void initView() {
        msgText = findViewById(R.id.msg_text);
    }

    public void setMsgText(String msg) {
        msgText.setText(msg);
    }
}
