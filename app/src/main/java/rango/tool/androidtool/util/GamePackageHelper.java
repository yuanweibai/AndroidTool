package rango.tool.androidtool.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import rango.tool.androidtool.ToolApplication;

public class GamePackageHelper {

    private static final String TAG = "GamePackageHelper";

    private static String sGamePackages = null;

    public static boolean isGame(String packageName) {
//        if (TextUtils.isEmpty(sGamePackages)) {
        initGamePackages();
//        }

        return sGamePackages.contains(packageName);
    }

    public static String initGamePackages() {
        long currentMills = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            inputStream = ToolApplication.getContext().getAssets().open("GamePackage.txt");
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            sGamePackages = new String(bytes);
            return sGamePackages;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "mills = " + (System.currentTimeMillis() - currentMills));
        }
        return null;
    }
}
