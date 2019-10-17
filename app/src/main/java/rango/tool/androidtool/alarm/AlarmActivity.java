package rango.tool.androidtool.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class AlarmActivity extends BaseActivity {

    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_layout);

        editText = findViewById(R.id.edit_text);

        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlarm();
            }
        });

        findViewById(R.id.show_notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.showNotification(AlarmActivity.this);
            }
        });
    }

    private void sendAlarm() {
        long time = System.currentTimeMillis() + Integer.valueOf(editText.getText().toString()) * 60 * 1000;
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 3,
                intent, 0);

        if (mgr != null) {
            mgr.set(AlarmManager.RTC_WAKEUP, time, pi);
        }
    }
}
