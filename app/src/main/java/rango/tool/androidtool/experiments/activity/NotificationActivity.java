package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.notification.ToolNotificationListenerService;

public class NotificationActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_layout);

        findViewById(R.id.start_listen_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolNotificationListenerService.openNotificationListenSettings(NotificationActivity.this);
            }
        });

        findViewById(R.id.rebind_listener_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolNotificationListenerService.toggleNotificationListenerService(NotificationActivity.this);
            }
        });
    }
}
