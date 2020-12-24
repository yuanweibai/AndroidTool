package rango.tool.androidtool.game;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import rango.kotlin.walk.WalkActivity;
import rango.kotlin.wanandroid.LaunchActivity;
import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.game.hero.GameHeroActivity;

public class AppMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        findViewById(R.id.hero_game_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GameHeroActivity.class);
            }
        });

        findViewById(R.id.wanAndroidBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LaunchActivity.class);
            }
        });

        findViewById(R.id.wanAndroidBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WalkActivity.class);
            }
        });
    }
}
