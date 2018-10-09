package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.view.StickerView;

public class StickerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StickerView stickerView = new StickerView(StickerActivity.this);
        setContentView(stickerView);
    }
}
