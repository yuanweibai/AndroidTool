package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class AnyThingActivity extends BaseActivity {

    private ViewStub viewStub;

    private View view;

    private int d = 0;

    @Override
    public void onAttachedToWindow() {

        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_any_thing_layout);


//


    }

}
