package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by keyurgolani on 5/12/17.
 */

public class PreferenceHandler {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static void putKey(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    private static String getKey(String key) {
        return sharedPreferences.getString(key, null);
    }

    private static void clearKey(String key) {
        editor.remove(key);
        editor.commit();
    }

    public static void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void putAccessKey(String uniqueID) {
        putKey("access_key", uniqueID);
    }

    public static void putVersion(String version) {
        putKey("version", version);
    }

    public static String getAccessKey() {
        return getKey("access_key");
    }

    public static String getVersion() {
        return getKey("version");
    }

    public static void clearAccessKey() {
        clearKey("access_key");
    }

}
