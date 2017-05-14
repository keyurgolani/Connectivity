package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;
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

        TabHost tab_host = (TabHost)findViewById(R.id.tabHost);
        tab_host.setup();

        //Tab 1
        TabHost.TabSpec tab1 = tab_host.newTabSpec("Tab One");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.icon_timeline));
        tab_host.addTab(tab1);


        //Tab 2
        TabHost.TabSpec tab2 = tab_host.newTabSpec("Tab Two");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.icon_msg));
        tab_host.addTab(tab2);

        //Tab 3
        TabHost.TabSpec tab3 = tab_host.newTabSpec("Tab Three");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.icon_friends));
        tab_host.addTab(tab3);

        //Tab 4
        TabHost.TabSpec tab4 = tab_host.newTabSpec("Tab Four");
        tab4.setContent(R.id.tab4);
        tab4.setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.icon_profile));
        tab_host.addTab(tab4);
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
