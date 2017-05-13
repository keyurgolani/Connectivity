package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by keyurgolani on 5/12/17.
 */

public class PreferenceHandler {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void putAccessKey(String uniqueID) {
        editor.putString("access_key", uniqueID);
        editor.commit();
    }

    public static String getAccessKey() {
        return sharedPreferences.getString("access_key", null);
    }

    public static void clearAccessKey() {
        editor.remove("access_key");
        editor.commit();
    }

}
