package rango.tool.androidtool.experiments.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.view.View;
import android.widget.RemoteViews;

import rango.tool.androidtool.BuildConfig;
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

        findViewById(R.id.float_notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloatNotification();
            }
        });

        findViewById(R.id.float_big_notification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloatBigNotification();
            }
        });
    }

    private void showNotification() {
        String channelId = createNotificationChannel("my_channel_id", "Channel", NotificationManager.IMPORTANCE_HIGH);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HHHHHHHHHH")
                .setContentText("fasdfadfadfa")
                .setDefaults(NotificationCompat.PRIORITY_DEFAULT);

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

    private void showFloatNotification() {
        // 默认高度 64dp
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com/"));
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom_view);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setAutoCancel(true)    //点击后关闭通知
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setFullScreenIntent(pi, true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder.setContent(remoteViews);
        } else {
            builder.setCustomContentView(remoteViews);
        }
        notificationManager.notify(3, builder.build());
    }

    private void showFloatBigNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com/"));
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_big_custom_view);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setAutoCancel(true)    //点击后关闭通知
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setFullScreenIntent(pi, true);

        Notification notification;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notification = builder.build();
            notification.bigContentView = remoteViews;
        } else {
            builder.setCustomBigContentView(remoteViews);
            notification = builder.build();
        }
        notificationManager.notify(3, notification);
    }
}
