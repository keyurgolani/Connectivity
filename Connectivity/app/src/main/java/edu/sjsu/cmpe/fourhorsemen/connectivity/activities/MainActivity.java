package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.LoginActivity;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.Utilities;

/**
 * Created by gauravchodwadia on 5/6/17.
 */

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_LOGIN = 0;
    static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceHandler.initSharedPreferences(getApplicationContext());
        PreferenceHandler.putVersion("1.0.0");
        // Check for app backward compatibility
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("version", PreferenceHandler.getVersion());
        RequestHandler.HTTPRequest(getApplicationContext(), "version", params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(Utilities.getVersionDiff(response.getString("version"), PreferenceHandler.getVersion()) > 0) {
                    // Open App Store for Latest Update
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {

            }
        });
        // Check if logged in
        if(PreferenceHandler.getAccessKey() == null) {
            requestLogin(getApplicationContext());
        }
    }

    private void requestLogin(Context applicationContext) {
        Intent intent = new Intent(applicationContext, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            if(PreferenceHandler.getAccessKey() != null) {
                Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG);
            } else {
                requestLogin(getApplicationContext());
            }
        }
    }
}
