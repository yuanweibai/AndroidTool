package rango.tool.androidtool.transition;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class OffsetActivity extends BaseActivity {

    private ImageView imageView;

    private int offset = 10;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offset_layout);

        imageView = findViewById(R.id.img_view);
        findViewById(R.id.set_btn).setOnClickListener(v -> {
            imageView.offsetTopAndBottom(offset);
            offset += 10;
        });

    }
}
