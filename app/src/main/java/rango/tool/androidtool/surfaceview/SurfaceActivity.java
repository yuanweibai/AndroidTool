package rango.tool.androidtool.surfaceview;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.surfaceview.lib.DrawableSurfaceView;

public class SurfaceActivity extends BaseActivity {

    private DrawableSurfaceView drawableSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_test_layout);

        drawableSurfaceView = findViewById(R.id.drawable_surface_view);

//        drawableSurfaceView.append(new FarmBgDrawBean());
        drawableSurfaceView.append(new ImageDrawBean());
    }

    @Override
    protected void onStart() {
        super.onStart();
        drawableSurfaceView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawableSurfaceView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawableSurfaceView.onDestroy();
    }
}
