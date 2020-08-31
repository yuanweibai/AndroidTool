package rango.tool.androidtool.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ImagePreviewActivity extends BaseActivity {

    private ScaleChildLayout scaleChildLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        scaleChildLayout = findViewById(R.id.root_view);

        scaleChildLayout.setOnTouchEventListener(this::finishAfterTransition);

        findViewById(R.id.image_view).setOnClickListener(v -> Toast.makeText(ImagePreviewActivity.this, "哈哈哈哈哈哈哈哈", Toast.LENGTH_LONG).show());
    }
}
