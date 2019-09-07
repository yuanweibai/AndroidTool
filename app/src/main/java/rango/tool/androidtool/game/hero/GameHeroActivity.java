package rango.tool.androidtool.game.hero;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.ActivityUtils;

public class GameHeroActivity extends BaseActivity {

    private GameHeroView gameHeroView;

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityUtils.setupTransparentStatusBarsForLmp(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameHeroView = new GameHeroView(GameHeroActivity.this);
        setContentView(gameHeroView);
    }
}
