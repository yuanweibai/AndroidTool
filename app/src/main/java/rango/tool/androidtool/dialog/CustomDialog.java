package rango.tool.androidtool.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import rango.tool.androidtool.R;

public class CustomDialog extends Dialog {

    private TextView titleText;

    public CustomDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_custom_layout);

        titleText = findViewById(R.id.title_text);

        findViewById(R.id.update_title_btn).setOnClickListener(v -> {
            Log.e("rango-dialog","thread = "+Thread.currentThread().getName());
            titleText.setText("更新后的标题");
        });

    }
}
