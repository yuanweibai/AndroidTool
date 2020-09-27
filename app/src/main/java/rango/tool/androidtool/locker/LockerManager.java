package rango.tool.androidtool.locker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.util.WindowUtil;
import rango.tool.common.utils.Permissions;

public class LockerManager {

    private static LockerManager instance;
    private View lockView;
    private WindowManager windowManager;


    private LockerManager() {
        windowManager = (WindowManager) ToolApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    public static LockerManager getInstance() {
        if (instance == null) {
            synchronized (LockerManager.class) {
                if (instance == null) {
                    instance = new LockerManager();
                }
            }
        }
        return instance;
    }

    public void lockScreen() {
        Context context = ToolApplication.getContext();
        if (!Permissions.isFloatWindowAllowed(context)) {
            Permissions.requestFloatWindowPermission(context);
            return;
        }
        lockView = LayoutInflater.from(context).inflate(R.layout.locker_layout, null);

        lockView.findViewById(R.id.clear_lock_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unLockScreen();
            }
        });
        windowManager.addView(lockView, FloatWindowCompat.getLockScreenParams());
    }

    public void unLockScreen() {
        windowManager.removeViewImmediate(lockView);
        lockView = null;
    }

}
