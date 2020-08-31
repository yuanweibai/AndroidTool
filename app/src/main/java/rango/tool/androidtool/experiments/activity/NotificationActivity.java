package rango.tool.androidtool.experiments.activity;

import android.app.NotificationManager;
import android.app.Service;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.notification.ToolNotificationListenerService;

public class NotificationActivity extends BaseActivity {


    private NotificationManager notificationManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_layout);

        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

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

        findViewById(R.id.clear_notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notificationManager.cancelAll();
            }
        });


    }
}
