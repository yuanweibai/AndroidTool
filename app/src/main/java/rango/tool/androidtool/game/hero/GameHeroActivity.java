package rango.tool.androidtool.game.hero;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.ActivityUtils;

public class GameHeroActivity extends BaseActivity {

    private GameHeroView gameHeroView;

    private ConstraintLayout failureLayout;

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityUtils.setupTransparentStatusBarsForLmp(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hero);

        gameHeroView = findViewById(R.id.game_hero_view);
        failureLayout = findViewById(R.id.failure_layout);
        gameHeroView.setOnStatusListener(() -> failureLayout.setVisibility(View.VISIBLE));

        findViewById(R.id.retry_btn).setOnClickListener(v -> {
            failureLayout.setVisibility(View.GONE);
            gameHeroView.retry();
        });

    }
}
