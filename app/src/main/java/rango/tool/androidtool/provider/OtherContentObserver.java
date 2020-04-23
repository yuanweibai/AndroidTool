package rango.tool.androidtool.provider;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class OtherContentObserver extends ContentObserver {

    public OtherContentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
    }
}
