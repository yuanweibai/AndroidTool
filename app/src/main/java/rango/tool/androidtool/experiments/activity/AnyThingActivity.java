package rango.tool.androidtool.experiments.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.any.Rango;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.market.MarketTools;

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

                startOtherActivity();

            }
        });
//
        findViewById(R.id.any_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketTools.getTools().startMarket(ToolApplication.getContext(),"com.colorphone.smooth.dialer.cn");
            }
        });

    }

    private void startOtherActivity() {
        Intent intent = new Intent();
        ComponentName cn = new ComponentName("com.dograise.richman.cn", "com.infini.pigfarm.lottery.LotteryWheelActivity");
        intent.setComponent(cn);
        startActivity(intent);
    }


}
