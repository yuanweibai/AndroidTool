package rango.tool.androidtool.transition;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;


public class ImageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
        findViewById(R.id.image_view).setOnClickListener(v -> {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ImageActivity.this, v, getString(R.string.preview_transition));
            Intent intent = new Intent(ImageActivity.this, ImagePreviewActivity.class);
            ActivityCompat.startActivity(ImageActivity.this, intent, optionsCompat.toBundle());
        });
    }
}
