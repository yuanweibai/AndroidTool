package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import rango.tool.androidtool.R;
import rango.tool.androidtool.any.Rango;
import rango.tool.androidtool.base.BaseActivity;

public class AnyThingActivity extends BaseActivity {

    private ViewStub viewStub;

    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_thing_layout);

        Rango rango = new Rango("name");
        Rango rango1 = new Rango("name");

        findViewById(R.id.show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("rango", "rango.hashCode = " + rango.hashCode());
                Log.e("rango", "rang1.hashCode = " + rango1.hashCode());

            }
        });
//
//        findViewById(R.id.hide_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.setVisibility(View.GONE);
//            }
//        });

    }

}
