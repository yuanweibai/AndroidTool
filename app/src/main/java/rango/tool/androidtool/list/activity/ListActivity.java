package rango.tool.androidtool.list.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.LifecycleActivity;
import rango.tool.androidtool.launchmodel.LaunchMode1Activity;
import rango.tool.androidtool.list.ListFragment;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class ListActivity extends LifecycleActivity {
    private ListFragment listFragment;

    static {
        TAG = "RangoActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        listFragment = new ListFragment();
        initFragment(R.id.fragment, listFragment);

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LaunchMode1Activity.class);
            }
        });
    }
}
