package rango.tool.androidtool.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class ToolAccessibilityService extends AccessibilityService {

    private static final String TAG = "ToolAccessibility";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.e(TAG, "onServiceConnected");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, "onAccessibilityEvent: packageName = " + event.getPackageName());

        AccessibilityNodeInfo eventNodeInfo = event.getSource();

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            Log.e(TAG, "click: text = " + eventNodeInfo.getText());
        }
        String packageName = String.valueOf(event.getPackageName());
        if (packageName.equals("com.xiaomi.market")) {
            AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();

            List<AccessibilityNodeInfo> nodeInfoList = rootNodeInfo.findAccessibilityNodeInfosByText("评论");
            for (AccessibilityNodeInfo node : nodeInfoList) {
                if (node != null) {
                    Rect rect = new Rect();
                    node.getBoundsInScreen(rect);

                    Log.e(TAG, "left = " + rect.left + ", top = " + rect.top + ", right = " + rect.right + ", bottom = " + rect.bottom);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "onInterrupt");
    }
}
