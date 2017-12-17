package rango.tool.androidtool.locker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import rango.tool.androidtool.R;
import rango.tool.androidtool.util.WindowUtil;

public class LockerManager {

    private static LockerManager instance;
    private View lockView;
    private WindowManager windowManager;
    private Context context;


    private LockerManager(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.context = context;
    }

    public static LockerManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LockerManager.class) {
                if (instance == null) {
                    instance = new LockerManager(context);
                }
            }
        }
        return instance;
    }

    public void lockScreen() {
        lockView = LayoutInflater.from(context).inflate(R.layout.locker_layout, null);
        windowManager.addView(lockView, WindowUtil.getLockScreenParams());
    }

    public void unLockScreen() {
        windowManager.removeViewImmediate(lockView);
    }

}
