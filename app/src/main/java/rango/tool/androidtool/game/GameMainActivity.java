package rango.tool.androidtool.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.game.hero.GameHeroActivity;

public class GameMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        findViewById(R.id.hero_game_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("rango", "dddddd---------ddddddd");
                int i = 0;
                if (i == 0) {
                    throw new IllegalStateException("hhhhhhhhhhhh");
                } else {

                }
                Log.e("rango", "eeeeeeee---------eeeeeeeee");

                startActivity(GameHeroActivity.class);
            }
        });
    }
}
