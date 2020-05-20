package rango.tool.androidtool.experiments.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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
                TimeZone tz = TimeZone.getDefault();
                String strTz = tz.getDisplayName(false, TimeZone.SHORT);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format.setTimeZone(tz);
                String date = format.format(new Date(System.currentTimeMillis()));

                long curTime = System.currentTimeMillis();
                String timeStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(new Date(curTime));

                Log.e("rango", "strTz = " + strTz + ", date = " + timeStr);

            }
        });
//
        findViewById(R.id.any_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
                int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);

                Log.e("rango", "zoneOffset = " + zoneOffset + ", dstOffset = " + dstOffset);
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
