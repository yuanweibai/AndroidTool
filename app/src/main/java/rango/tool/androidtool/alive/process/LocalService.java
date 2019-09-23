package rango.tool.androidtool.alive.process;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import rango.tool.androidtool.KeepAliveConnection;

public class LocalService extends Service {

    private static final String TAG = "LocalService";

    private IBinder binder = new LocalAidl();

    private KeepAliveConnection aliveBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aliveBinder = KeepAliveConnection.Stub.asInterface(service);
            try {
                Toast.makeText(LocalService.this, "Local: " + aliveBinder.getServiceName(), Toast.LENGTH_LONG).show();
            } catch (RemoteException ignored) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(LocalService.this, AliveService.class));
            bindService(new Intent(LocalService.this, AliveService.class), connection, Context.BIND_IMPORTANT);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, new Notification());
        bindService(new Intent(LocalService.this, AliveService.class), connection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    private static class LocalAidl extends KeepAliveConnection.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return "LocalService";
        }
    }
}
