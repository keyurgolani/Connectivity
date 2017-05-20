package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.MainActivity;

/**
 * Created by keyurgolani on 5/13/17.
 */

public class Utilities {

    public static String TAG = Utilities.class.toString();

    public static int getVersionDiff(String supported, String current) {
        String[] vals1 = supported.split("\\.");
        String[] vals2 = current.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    public static void cacheProfile(final Context context) {
        HashMap<String, String> profile_params = new HashMap<String, String>();
        profile_params.put("unique_id", PreferenceHandler.getAccessKey());
        RequestHandler.HTTPRequest(context, ProjectProperties.METHOD_FETCH_PROFILE, profile_params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(response.getInt("status_code") == 200) {
                    JSONObject profile_array = response.getJSONArray("message").getJSONObject(0);
                    PreferenceHandler.putProfile(
                            profile_array.getString("profile_id"),
                            profile_array.getString("account"),
                            profile_array.getString("f_name"),
                            profile_array.getString("l_name"),
                            profile_array.getString("location"),
                            profile_array.getString("profession"),
                            profile_array.getString("screen_name"),
                            profile_array.getString("about_me"),
                            profile_array.getString("dob"),
                            profile_array.getString("gender"),
                            profile_array.getString("timestamp"));
                    getBase64Image(context, profile_array.getString("profile_pic"), new ResponseHandler() {
                        @Override
                        public void handleSuccess(JSONObject response) throws Exception {
                            if(response.getInt("status_code") == 200) {
                                PreferenceHandler.putKey("profile_pic", response.getString("message"));
                            }
                        }

                        @Override
                        public void handleError(Exception e) throws Exception {
                            // Don't tell the user if we can't cache the profile ;)
                        }
                    });
                } else {
                    // Don't tell the user if we can't cache the profile ;)
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                e.printStackTrace();
            }
        });
    }

    public static void getBase64Image(Context context, String imageID, ResponseHandler rh) {
        HashMap<String, String> profile_params = new HashMap<String, String>();
        profile_params.put("unique_id", PreferenceHandler.getAccessKey());
        profile_params.put("photo_id", imageID);
        RequestHandler.HTTPRequest(context, ProjectProperties.METHOD_GET_PHOTO, profile_params, rh);
    }
}
