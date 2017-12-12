package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.view.AutoScrollCircleLayout;

public class AutoScrollActivity extends BaseActivity {

    private AutoScrollCircleLayout autoScrollLayout;
    private List<String> picList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_scroll_activity);
        initView();

    }

    private void initView() {
        autoScrollLayout = findViewById(R.id.auto_scroll_layout);
        setPicData();
        autoScrollLayout.setData(picList);

        findViewById(R.id.play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoScrollLayout.startAutoPlay();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoScrollLayout.stopAutoPlay();
            }
        });
    }

    private void setPicData() {
        picList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_carousel/S7_IMG.png");
        picList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_carousel/merrychristmas.png");
        picList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_carousel/emoji.png");
        picList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_carousel/butterfly.png");
    }
}
