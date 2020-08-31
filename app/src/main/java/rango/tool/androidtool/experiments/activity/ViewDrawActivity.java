package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ViewDrawActivity extends BaseActivity {

    private static final String TAG = ViewDrawActivity.class.getSimpleName();

    Handler handler;

    TextView textView;
    TextView resultText;
    TextView handlerResultText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_view_draw_layout);

        handler = new Handler();

        textView = findViewById(R.id.text_view);
//        resultText = findViewById(R.id.result_text);
        resultText = findViewById(getResources().getIdentifier("result_text", "id", getPackageName()));
//        handlerResultText = findViewById(R.id.handler_result_text);
        handlerResultText = findViewById(getResources().getIdentifier("handler_result_text", "id", getPackageName()));

    }

    @Override
    protected void onResume() {
        super.onResume();

//        textView.post(() -> {
//            int height = textView.getHeight();
//            int width = textView.getWidth();
//
//            resultText.setText("view: height = " + height + ", width = " + width);
//        });

        resultText.setText("onResume: height = " + textView.getHeight() + ", width = " + textView.getWidth());

        handler.post(() -> {
            int height = textView.getHeight();
            int width = textView.getWidth();

            handlerResultText.setText("handler: height = " + height + ", width = " + width);
        });
    }
}
