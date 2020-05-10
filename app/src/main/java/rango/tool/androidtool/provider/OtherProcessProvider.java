package rango.tool.androidtool.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.ToolApplication;
import rango.tool.common.utils.Worker;

public class OtherProcessProvider extends ContentProvider {

    private static final String TAG = OtherProcessProvider.class.getSimpleName();

    public static final String AUTHORITIES = "rango.tool.androidtool.provider.OtherProcessProvider";

    private String mValue = "OtherProcess";

    public static Uri createRemoteConfigContentUri() {
        return Uri.parse("content://" + AUTHORITIES);
    }

    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate(), process: " + android.os.Process.myPid());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = ToolApplication.getContext().getContentResolver().call(OtherProcessProvider.createRemoteConfigContentUri(), "rango_r", null, null);
            }
        });
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query()");
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(TAG, "getType()");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(TAG, "insert()");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "delete()");
        return 0;
    }

    @Override
    public synchronized int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "update()");
        return 0;
    }

    @Nullable
    @Override
    public synchronized Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Log.e(TAG, "call: method = " + method + ", extras = " + extras);
        Bundle bundle = new Bundle();
        switch (method) {
            case "rango_r":
                bundle.putString("rango_read", mValue);
                return bundle;
            case "rango_w":
                mValue = extras.getString("rango_write");
                return bundle;
            default:
                return bundle;
        }
    }
}
