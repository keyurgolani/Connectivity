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

/**
 * Created by gauravchodwadia on 5/6/17.
 */

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceHandler.initSharedPreferences(getApplicationContext());
        //TODO: check if the user is logged in or not
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
