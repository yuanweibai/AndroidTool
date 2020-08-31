package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.view.StickerView;

public class StickerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StickerView stickerView = new StickerView(StickerActivity.this);
//        ImageView imageView = new ScalableImageView(StickerActivity.this);
//        setContentView(stickerView);

        setContentView(R.layout.activity_sticker_layout);

//        findViewById(R.id.image_btn).setOnClickListener(v -> {
//            Toast.makeText(StickerActivity.this, "ddddd", Toast.LENGTH_LONG).show();
//        });

    }
}
