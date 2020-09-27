package rango.tool.androidtool.experiments.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.experiments.TestActivity;
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

        findViewById(R.id.show_notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });
    }

    private void showNotification() {
        String channelId = createNotificationChannel("my_channel_id", "Channel", NotificationManager.IMPORTANCE_HIGH);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HHHHHHHHHH")
                .setContentText("fasdfadfadfa")
                .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
                ;

        Intent intent = new Intent(this, TestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setFullScreenIntent(pendingIntent, true);

        Notification notification = builder.build();

        notificationManager.notify(1909, notification);
    }

    private String createNotificationChannel(String channelID, String channelNAME, int level) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }
}
