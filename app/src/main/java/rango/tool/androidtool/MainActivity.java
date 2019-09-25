package rango.tool.androidtool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.coordinator.CoordinatorActivity;
import rango.tool.androidtool.experiments.TestActivity;
import rango.tool.androidtool.game.GameMainActivity;
import rango.tool.androidtool.game.hero.GameHeroActivity;
import rango.tool.androidtool.list.activity.ListActivity;
import rango.tool.androidtool.list.activity.RecyclerActivity;
import rango.tool.androidtool.touch.TouchActivity;
import rango.tool.androidtool.transition.OffsetActivity;
import rango.tool.androidtool.transition.TransitionActivity;

public class MainActivity extends BaseActivity {

    private LinearLayout containerView;

    private static final boolean IS_TEST = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (IS_TEST) {
            startActivity(GameHeroActivity.class);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
//        WindowUtil.hideStatusBar(this);

        containerView = findViewById(R.id.container_view);

        findViewById(R.id.recycler_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.list_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.transition_btn).setOnClickListener(v -> startActivity(TransitionActivity.class));
        findViewById(R.id.coordinator_btn).setOnClickListener(v -> startActivity(CoordinatorActivity.class));
        findViewById(R.id.test_btn).setOnClickListener(v -> {
            startActivity(TestActivity.class);
        });

        findViewById(R.id.add_view_btn).setOnClickListener(v -> addTestView());
        findViewById(R.id.offset_btn).setOnClickListener(v -> startActivity(OffsetActivity.class));
        findViewById(R.id.touch_btn).setOnClickListener(v -> startActivity(TouchActivity.class));
        findViewById(R.id.game_btn).setOnClickListener(v -> startActivity(GameMainActivity.class));
    }

    private void addTestView() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.solon);
        containerView.addView(imageView, 1);
    }
}
