package rango.tool.androidtool;

import android.content.Intent;
import android.os.Bundle;

import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.experiments.TestActivity;
import rango.tool.androidtool.list.activity.ListActivity;
import rango.tool.androidtool.list.activity.RecyclerActivity;
import rango.tool.androidtool.transition.TransitionActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.recycler_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.list_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.transition_btn).setOnClickListener(v -> startActivity(TransitionActivity.class));
        findViewById(R.id.test_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);
        });
    }
}
