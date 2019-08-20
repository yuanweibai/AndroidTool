package rango.tool.androidtool.ipc.aidi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.IMyAidl;

public class AidlService extends Service {

    private static final String TAG = AidlService.class.getSimpleName();

    private IMyAidl.Stub binder = new IMyAidl.Stub() {
        @Override
        public void sendMsg(String msg) throws RemoteException {
            Log.e(TAG, "sendMsg: " + msg + ", processId = " + android.os.Process.myPid());
        }

        @Override
        public String getMsg() throws RemoteException {
            int pid = android.os.Process.myPid();
            Log.e(TAG, "getMsg - processId = " + pid);
            return "aidl_service_" + pid;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
