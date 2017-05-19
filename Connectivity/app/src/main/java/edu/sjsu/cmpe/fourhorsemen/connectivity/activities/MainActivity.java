package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import android.view.MenuItem;
import android.content.Intent;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.LoginActivity;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Message;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Post;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.ProfileFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.MessageFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.Utilities;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements PostFragment.OnListFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, MessageFragment.OnListFragmentInteractionListener {

    static final int REQUEST_LOGIN = 0;
    static final int REQUEST_APP_INTRO = 1;
    static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_timeline:
                    selectedFragment = PostFragment.newInstance(1);
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = PostFragment.newInstance(1);
                    break;
                case R.id.navigation_profile:
                    selectedFragment = ProfileFragment.newInstance();
                    break;
                case R.id.navigation_chat:
                    selectedFragment = MessageFragment.newInstance(1);
                    break;
                default:
                    selectedFragment = PostFragment.newInstance(1);

            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, selectedFragment);
            transaction.commit();
            return true;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProjectProperties.init(getBaseContext());
        PreferenceHandler.initSharedPreferences(getApplicationContext());
        PreferenceHandler.putVersion("1.0.0");
        // Check for app backward compatibility
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("version", PreferenceHandler.getVersion());
        RequestHandler.HTTPRequest(getApplicationContext(), ProjectProperties.METHOD_VERSION, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(Utilities.getVersionDiff(response.getString("version"), PreferenceHandler.getVersion()) > 0) {
                    // Open App Store for Latest Update
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                // Do Nothing
            }
        });

        // Check if logged in
        if(PreferenceHandler.getAccessKey() == null) {
            requestLogin(getApplicationContext());
        } else {
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, PostFragment.newInstance(1));
            transaction.commit();
        }



        if(PreferenceHandler.getFirstLaunch() == null) {
            openAppIntro(getApplicationContext());
        }

    }

    private void openAppIntro(Context applicationContext) {
        Intent intent = new Intent(applicationContext, AppIntro.class);
        startActivityForResult(intent, REQUEST_APP_INTRO);
        PreferenceHandler.putFirstLaunch();
    }

    private void requestLogin(Context applicationContext) {
        Intent intent = new Intent(applicationContext, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_APP_INTRO) {
            PreferenceHandler.putFirstLaunch();
        }
        if(requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            if(PreferenceHandler.getAccessKey() != null) {
                Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG);
            } else {
                requestLogin(getApplicationContext());
            }
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, PostFragment.newInstance(1));
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public void onListFragmentInteraction(Post item) {

    }

    @Override
    public void onListFragmentInteraction(Message item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
