package rango.tool.androidtool.game.hero;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.ActivityUtils;

public class GameHeroActivity extends BaseActivity {

    private GameHeroView gameHeroView;
    private ConstraintLayout startLayout;
    private ConstraintLayout failureLayout;
    private TextView scoreText;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityUtils.setupTransparentStatusBarsForLmp(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hero);

        startLayout = findViewById(R.id.start_layout);
        scoreText = findViewById(R.id.score_text);
        gameHeroView = findViewById(R.id.game_hero_view);
        failureLayout = findViewById(R.id.failure_layout);
        gameHeroView.setOnStatusListener(new GameHeroView.OnStatusListener() {
            @Override
            public void onSuccess(int currentScore) {
                scoreText.setText(String.valueOf(currentScore));
            }

            @Override
            public void onFailure() {
                failureLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPerfect(int currentScore) {
                scoreText.setText(String.valueOf(currentScore));
            }
        });

        findViewById(R.id.retry_btn).setOnClickListener(v -> {
            failureLayout.setVisibility(View.GONE);
            gameHeroView.retry();
            scoreText.setText("0");
        });

        findViewById(R.id.start_btn).setOnClickListener(v -> {
            startLayout.setVisibility(View.GONE);
            gameHeroView.start();
        });

    }
}
