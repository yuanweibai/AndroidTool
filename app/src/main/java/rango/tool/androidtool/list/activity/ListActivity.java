package rango.tool.androidtool.list.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.list.ListFragment;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class ListActivity extends BaseActivity {
    private ListFragment listFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        listFragment = new ListFragment();
        initFragment(R.id.fragment, listFragment);
    }
}
