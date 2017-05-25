package edu.sjsu.cmpe.fourhorsemen.connectivity.utilities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
                    Log.d("Profile", profile_array.toString());
                    PreferenceHandler.putProfile(
                            profile_array.getString("profile_id"),
                            profile_array.getString("account"),
                            profile_array.getString("fullname"),
                            profile_array.getString("location"),
                            profile_array.getString("profession"),
                            profile_array.getString("screen_name"),
                            profile_array.getString("interests"),
                            profile_array.getString("about_me"),
                            profile_array.getString("profile_pic"),
                            profile_array.getString("timestamp"));
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

    public static String encodePhoto(Context context, Uri photoUri) {
        String encodedString = "";
        final InputStream imageStream;
        try {
            imageStream = context.getContentResolver().openInputStream(photoUri);
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            final Bitmap photo = Bitmap.createScaledBitmap(image, image.getScaledWidth(DisplayMetrics.DENSITY_260), image.getScaledHeight(DisplayMetrics.DENSITY_300), true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100,baos);
            encodedString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    public static String encodePhoto(Context context, Bitmap photo) {
        String encodedString = "";
        final InputStream imageStream;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Bitmap image = Bitmap.createScaledBitmap(photo, photo.getScaledWidth(DisplayMetrics.DENSITY_260), photo.getScaledHeight(DisplayMetrics.DENSITY_300), true);

        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        encodedString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return encodedString;
    }
}
