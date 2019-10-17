package rango.tool.androidtool.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import rango.tool.androidtool.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
        Toast.makeText(context, "hhhhhhhhhhh", Toast.LENGTH_LONG).show();
    }

    public static void showNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "com.rango.androidtool.frontDeskNotification";
            NotificationChannel channel = new NotificationChannel(channelId, "rango", NotificationManager.IMPORTANCE_LOW);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }

            Notification notification = new Notification.Builder(context, channelId)
                    .setSmallIcon(R.drawable.test_3)
                    .setContentTitle("闹钟测试")
                    .setContentText("Running....")
                    .build();
            if (manager != null) {
                manager.notify(1, notification);
            }
        } else {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("闹钟测试");
            builder.setContentText("Service is running...");
            if (manager != null) {
                manager.notify(1, builder.build());
            }
        }
    }
}
