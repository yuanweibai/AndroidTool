package rango.tool.androidtool.touch;

import android.os.Bundle;
import androidx.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class TouchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_layout);
//        findViewById(R.id.image_view).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                Log.e("TouchActivity","ddddddddddddddd");
//            }
//        });
    }
}
