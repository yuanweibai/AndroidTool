package rango.tool.androidtool.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import rango.tool.androidtool.ToolApplication;

public class SharedPreferenceHelper {

    private static final String PREFERENCE_FILE = "preference_file";
    private static SharedPreferenceHelper instance;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    private SharedPreferenceHelper() {
        sharedPreferences = ToolApplication.getContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferenceHelper getInstance() {
        if (instance == null) {
            synchronized (SharedPreferenceHelper.class) {
                if (instance == null) {
                    instance = new SharedPreferenceHelper();
                }
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
