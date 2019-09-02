package rango.tool.androidtool.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.lang.reflect.Field;

import rango.tool.common.utils.ReflectionHelper;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationInfoBean {

    public String packageId;

    public long idInDB;
    public int notificationId;
    public String tag;
    public Notification notification;
    public PendingIntent contentIntent;
    public String title;
    public String text;
    public String key;
    public long postTime;
    public Bitmap icon;

    public NotificationInfoBean() {
    }

    public static NotificationInfoBean valueOf(StatusBarNotification statusBarNotification) {
        NotificationInfoBean notificationInfo = new NotificationInfoBean();

        notificationInfo.packageId = statusBarNotification.getPackageName();
        notificationInfo.postTime = statusBarNotification.getPostTime();
        Notification notification = statusBarNotification.getNotification();
        notificationInfo.notification = notification;
        notificationInfo.contentIntent = notification.contentIntent;
        notificationInfo.notificationId = statusBarNotification.getId();
        notificationInfo.tag = statusBarNotification.getTag();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationInfo.icon = getNotificationLargeIcon(notification.getLargeIcon());
        } else {
            notificationInfo.icon = notification.largeIcon;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationInfo.key = statusBarNotification.getKey();
        }

        Bundle extras;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            extras = notification.extras;
        } else {
            extras = getExtras(notification);
        }

        if (null != extras) {
            notificationInfo.title = getExtrasTitle(extras);
            notificationInfo.text = getExtrasText(extras);
        }
        return notificationInfo;
    }


    private static Bundle getExtras(@NonNull final Notification notification) {
        try {
            Field extrasField = ReflectionHelper.getField(notification.getClass(), "extras");
            return (Bundle) extrasField.get(notification);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static Bitmap getNotificationLargeIcon(Icon largeIcon) {
        try {
            Field extrasField = ReflectionHelper.getField(Icon.class, "mObj1");
            return (Bitmap) extrasField.get(largeIcon);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getExtrasTitle(@NonNull final Bundle extras) {
        String result;

        CharSequence title = extras.getCharSequence("android.title");
        CharSequence bigTitle = extras.getCharSequence("android.title.big");

        if (!TextUtils.isEmpty(title)) {
            result = !TextUtils.isEmpty(bigTitle) ? String.valueOf(bigTitle) : String.valueOf(title);
        } else {
            result = !TextUtils.isEmpty(bigTitle) ? String.valueOf(bigTitle) : "";
        }

        return result;
    }

    private static String getExtrasText(@NonNull final Bundle extras) {
        CharSequence text = extras.getCharSequence("android.text");
        if (!TextUtils.isEmpty(text)) {
            return text.toString();
        }

        CharSequence textLines = extras.getCharSequence("android.textLines");
        if (!TextUtils.isEmpty(textLines)) {
            return textLines.toString();
        }

        CharSequence subText = extras.getCharSequence("android.subText");
        if (!TextUtils.isEmpty(subText)) {
            return subText.toString();
        }

        return null;
    }
}
