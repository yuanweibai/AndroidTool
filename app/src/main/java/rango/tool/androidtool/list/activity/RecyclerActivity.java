package rango.tool.androidtool.list.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.list.RecyclerFragment2;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class RecyclerActivity extends BaseActivity {

    private RecyclerFragment2 recyclerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        recyclerFragment = new RecyclerFragment2();
        initFragment(R.id.fragment, recyclerFragment);
    }
}
