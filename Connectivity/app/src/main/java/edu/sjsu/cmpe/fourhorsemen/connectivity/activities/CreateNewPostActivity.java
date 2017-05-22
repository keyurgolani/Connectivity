package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    static final int RESULT_PHOTO_FROM_GALLARY_POST = 503;
    static final int RESULT_PHOTO_FROM_CAMERA_POST = 504;

    @Bind(R.id.new_post_content) EditText newPost;
    @Bind(R.id.user_photo) ImageView user_photo;
    @Bind(R.id.sn_label) TextView user_name;
    @Bind(R.id.btn_add_photo_camera)
    Button btnCamera;
    @Bind(R.id.btn_add_photo_gallery) Button btnGallery;
    @Bind(R.id.img_post) ImageView imgPost;

    String newPostStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        byte[] decodedString = Base64.decode(PreferenceHandler.getProfilePic(), Base64.DEFAULT);
        user_photo.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        user_name.setText(PreferenceHandler.getScreenName());


        View.OnClickListener photoBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add_photo_camera:
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, RESULT_PHOTO_FROM_CAMERA_POST);
                        break;
                    case R.id.btn_add_photo_gallery:
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , RESULT_PHOTO_FROM_GALLARY_POST);
                        break;
                }
            }
        };

        btnCamera.setOnClickListener(photoBtnListener);
        btnGallery.setOnClickListener(photoBtnListener);
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        finish();
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case RESULT_PHOTO_FROM_CAMERA_POST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgPost.setImageURI(selectedImage);
                    imgPost.setVisibility(View.VISIBLE);
                    //TODO: Code to send post with photo to the database
                }

                break;
            case RESULT_PHOTO_FROM_GALLARY_POST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgPost.setImageURI(selectedImage);
                    imgPost.setVisibility(View.VISIBLE);
                    //TODO: Code to send post with photo to the database

                }
                break;
        }
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
