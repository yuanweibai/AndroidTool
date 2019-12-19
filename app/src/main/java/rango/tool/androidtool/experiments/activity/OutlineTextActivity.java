package rango.tool.androidtool.experiments.activity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class OutlineTextActivity extends BaseActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outline_text);

        textView = findViewById(R.id.text_view);

//        TextPaint paint = textView.getPaint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(3);
//        textView.setText("秒变");
    }
}
