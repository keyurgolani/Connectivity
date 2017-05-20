package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

public class CreateNewPostActivity extends AppCompatActivity {

    private static String TAG = CreateNewPostActivity.class.getSimpleName();

    @Bind(R.id. new_post_content) EditText newPost;
    @Bind(R.id.user_photo) ImageView user_photo;
    @Bind(R.id.user_name) TextView user_name;
    String newPostStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post to ConActivity");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        byte[] decodedString = Base64.decode(PreferenceHandler.getProfilePic(), Base64.DEFAULT);
        user_photo.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        user_name.setText(PreferenceHandler.getScreenName());
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
        if(menuItem.getItemId() == 1000) {
            doAddPost();
        }
        if(getParent() != null) {
            getParent().finish();
        }
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
