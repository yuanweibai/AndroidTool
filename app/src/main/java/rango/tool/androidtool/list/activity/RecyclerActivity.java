package rango.tool.androidtool.list.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.list.RecyclerFragment;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class RecyclerActivity extends BaseActivity {

    private RecyclerFragment recyclerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        recyclerFragment = new RecyclerFragment();
        initFragment(R.id.fragment, recyclerFragment);
    }
}
