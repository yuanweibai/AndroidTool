package rango.tool.common.utils;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This helper class wraps {@link SharedPreferences} and:
 * <p/>
 * (1) Provides access to multiple preferences files with cached helper instances.
 * (2) Is thread safe.
 * {@link #putStringList(String, List)} and {@link #getStringList(String)}.
 * <p>
 * This class is only for (a) preferences files used in single process, (b) the default preferences file. Don't use this
 * for preferences file used in multi-process.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Preferences {

    private static final String DEFAULT_FILE_NAME = "tool_prefs";

    private static Map<String, Preferences> sHelpersCache = new HashMap<>();

    private SharedPreferences mAndroidPrefs;

    /**
     * {@link Integer} has an internal cache to support the object identity semantics of autoboxing for values between
     * -128 and 127 (inclusive) as required by JLS. We use the non-negative half of these cached objects as segmented
     * locks by mapping pref key strings to them. This is to avoid applying a global lock for all (unrelated) keys.
     */
    private static final int INTEGER_CACHE_UPPER_BOUND = 128;

    /**
     * Get Preferences by filename.
     */
    public synchronized static Preferences get(String filename) {
        // Elements in HashMap's backing array do not have a volatile semantic.
        // So double checked locking is not safe here.
        Preferences prefs = sHelpersCache.get(filename);
        if (prefs == null) {
            prefs = new Preferences(CommonManager.getInstance().getApplicationContext().getSharedPreferences(filename, 0));
            sHelpersCache.put(filename, prefs);
        }
        return prefs;
    }

    public static Preferences getDefault() {
        return get(DEFAULT_FILE_NAME);
    }

    // Single process, use Android SharedPreferences instance to avoid ContentProvider overhead
    private Preferences(SharedPreferences prefs) {
        mAndroidPrefs = prefs;
    }

    /**
     * When you need to edit multiple values in a SharedPreferences file at once, use this method
     * to retrieve the editor of Android SharedPreferences instance. Don't use putXXX() methods
     * below or the underlying file would be rewritten multiple times (which is a big waste).
     */
    public SharedPreferences.Editor edit() {
        return mAndroidPrefs.edit();
    }

    /**
     * Execute the given action only once for the given token.
     * <p>
     * Note that this method is thread safe only with token consumption.
     * Action execution should be synchronized by caller if necessary.
     *
     * @param action The action to perform.
     * @param token  The identifier on which the action can be performed only once.
     * @return {@code true} if the action is performed. {@code false} if the action has already been done before and
     * not performed this time.
     */
    public boolean doOnce(@NonNull Runnable action, String token) {
        boolean run = false;
        synchronized (getLock(token)) {
            if (!mAndroidPrefs.getBoolean(token, false)) {
                mAndroidPrefs.edit().putBoolean(token, true).apply();
                run = true;
            }
        }
        if (run) {
            action.run();
        }
        return run;
    }


    public boolean contains(String key) {
        return mAndroidPrefs.contains(key);
    }

    public void remove(String key) {
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().remove(key).apply();
        }
    }

    public int getInt(String key, int defaultValue) {
        return mAndroidPrefs.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putInt(key, value).apply();
        }
    }

    public long getLong(String key, long defaultValue) {
        return mAndroidPrefs.getLong(key, defaultValue);
    }

    public void putLong(String key, long value) {
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putLong(key, value).apply();
        }
    }

    public float getFloat(String key, float defaultValue) {
        return mAndroidPrefs.getFloat(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putFloat(key, value).apply();
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mAndroidPrefs.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putBoolean(key, value).apply();
        }
    }

    public String getString(String key, String defaultValue) {
        return mAndroidPrefs.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putString(key, value).apply();
        }
    }

    public List<String> getStringList(String key) {
        String listCsv = mAndroidPrefs.getString(key, "");
        return Strings.csvToStringList(listCsv);
    }

    public void putStringList(String key, List<String> stringList) {
        if (stringList != null) {
            String listCsv = Strings.stringListToCsv(stringList);
            synchronized (getLock(key)) {
                mAndroidPrefs.edit().putString(key, listCsv).apply();
            }
        }
    }

    public void addStringToList(String key, String value) {
        if (value != null) {
            synchronized (getLock(key)) {
                List<String> stringList = getStringList(key);
                if (!stringList.contains(value)) {
                    stringList.add(value);
                    putStringList(key, stringList);
                }
            }
        }
    }

    public boolean removeStringFromList(String key, String value) {
        if (value != null) {
            boolean contains;
            synchronized (getLock(key)) {
                List<String> stringList = getStringList(key);
                contains = stringList.remove(value);
                putStringList(key, stringList);
            }
            return contains;
        }
        return false;
    }

    public void addMap(String key, Map<String, String> inputMap) {
        Map<String, String> savedMap = getMap(key);
        for (Map.Entry<String, String> entry : inputMap.entrySet()) {
            savedMap.put(entry.getKey(), entry.getValue());
        }
        JSONObject jsonObject = new JSONObject(savedMap);
        String jsonString = jsonObject.toString();
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putString(key, jsonString).apply();
        }
    }

    public void putMap(String key, Map<String, String> inputMap) {
        JSONObject jsonObject = new JSONObject(inputMap);
        String jsonString = jsonObject.toString();
        synchronized (getLock(key)) {
            mAndroidPrefs.edit().putString(key, jsonString).apply();
        }
    }

    public Map<String, String> getMap(String key) {
        Map<String, String> outputMap = new HashMap<>();
        try {
            String jsonString;
            jsonString = mAndroidPrefs.getString(key, (new JSONObject()).toString());
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String k = keysItr.next();
                String v = (String) jsonObject.get(k);
                outputMap.put(k, v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    public Map<String, ?> getAll() {
        return mAndroidPrefs.getAll();
    }

    private Object /* Integer */ getLock(String key) {
        return key.hashCode() % INTEGER_CACHE_UPPER_BOUND;
    }
}
