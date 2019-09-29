package rango.tool.androidtool.memoryleak;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class MemoryLeakActivity extends BaseActivity {

    public static Activity activity;

    private static List<Bitmap> sBitmapList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak_layout);

        findViewById(R.id.btn_1).setOnClickListener(v -> startActivity(HandlerMemoryLeakActivity.class));
        findViewById(R.id.btn_2).setOnClickListener(v -> startActivity(StaticMemoryLeakActivity.class));

        findViewById(R.id.oom_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                makeOOM();
            }
        });
    }

    private void makeOOM() {
        sBitmapList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }

                    Bitmap bitmap = BitmapFactory.decodeResource(MemoryLeakActivity.this.getResources(), R.drawable.wallpaper);
                    sBitmapList.add(bitmap);
                }
            }
        }).start();
    }
}
