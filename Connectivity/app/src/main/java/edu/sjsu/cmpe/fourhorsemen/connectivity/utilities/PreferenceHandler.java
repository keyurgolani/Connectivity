package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

//import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Location;

/**
 * Created by keyurgolani on 5/12/17.
 */

public class PreferenceHandler {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static void putKey(String key, String value) {
        if(value!="null"){
            editor.putString(key, value);
            editor.commit();
        }

    }

    private static String getKey(String key) {
        return sharedPreferences.getString(key, null);
    }

    private static void clearKey(String key) {
        if(sharedPreferences.contains(key)) {
            editor.remove(key);
            editor.commit();
        }
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

    public static String getFirstLaunch() {
        return getKey("first_launch");
    }

    public static void putFirstLaunch() {
        putKey("first_launch", "1");
    }

    public static void clearFirstLaunch() {
        clearKey("first_launch");
    }

    public static void putProfile( 
             String profile_id,
             String account,
             String f_name,
             String l_name,
             String profile_pic,
             String location ,
             String profession,
             String screen_name,
             String about_me,
             String dob,
             String gender,
             String timestamp){

        putKey("profile_id", profile_id);
        putKey("account",account);
        putKey("f_name",f_name);
        putKey("l_name",l_name);
        putKey("profile_pic",profile_pic);
        putKey("location",location );
        putKey("screen_name",screen_name);
        putKey("profession",profession);
        putKey("about_me",about_me);
        putKey("dob",dob);
        putKey("gender",gender);
        putKey("timestamp",timestamp);



    }

    public static void clearProfile(){
        clearKey("profile_id");
        clearKey("account");
        clearKey("f_name");
        clearKey("l_name");
        clearKey("profile_pic");
        clearKey("location ");
        clearKey("profession");
        clearKey("screen_name");
        clearKey("about_me");
        clearKey("dob");
        clearKey("gender");
        clearKey("timestamp");
    }

    public static String getProfileID(){
        return getKey("profile_id");
    }




    
}
