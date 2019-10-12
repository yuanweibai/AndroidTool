package rango.tool.androidtool.wallpaper;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.IOException;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class WallpaperActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_layout);

        findViewById(R.id.set_static_wallpaper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WallpaperManagerProxy.getInstance().setSystemBitmap(WallpaperActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
