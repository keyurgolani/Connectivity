package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

public class CreateNewPostActivity extends AppCompatActivity {

    @Bind(R.id. new_post_content) EditText newPost;
    String newPostStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post to ConActivity");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newPost = (EditText) findViewById(R.id.new_post_content);
        newPost.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                newPost.setText("");
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, R.string.str_post);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        newPostStr = newPost.getText().toString();
        doAddPost();
        getParent().finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    public void doAddPost(){
        HashMap<String, String> params = new HashMap<String, String>();
        String unique_id = PreferenceHandler.getAccessKey();
        params.put("post", newPostStr);
        params.put("unique_id",unique_id);
        RequestHandler.HTTPRequest(getApplicationContext(), ProjectProperties.METHOD_ADD_POST, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws JSONException {
                switch (response.getInt("status_code")) {
                    case 200:
                        onAddPostSuccess();
                        break;

                }
            }

            @Override
            public void handleError(Exception e) {
                e.printStackTrace();

            }
        });
    }

    public void onAddPostSuccess(){

    }
}
