package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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

public class CreateNewMessageActivity extends AppCompatActivity {

    private static String TAG = CreateNewMessageActivity.class.getSimpleName();

    @Bind(R.id.et_to)
    EditText etTo;
    @Bind(R.id.et_sub)
    EditText etSubject;
    @Bind(R.id.et_msg)
    EditText etMessage;

    String toStr, subjectStr, messageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, R.string.str_post);
            MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        toStr = etTo.getText().toString();
        subjectStr = etSubject.getText().toString();
        messageStr = etSubject.getText().toString();

        if(menuItem.getItemId() == 1000) {
            //doAddPost();
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);
        }
        finish();
        return super.onOptionsItemSelected(menuItem);
    }



//    public void doAddPost(){
//        HashMap<String, String> params = new HashMap<String, String>();
//        String unique_id = PreferenceHandler.getAccessKey();
//        params.put("post", newPostStr);
//        params.put("unique_id",unique_id);
//        RequestHandler.HTTPRequest(getApplicationContext(), ProjectProperties.METHOD_ADD_POST, params, new ResponseHandler() {
//            @Override
//            public void handleSuccess(JSONObject response) throws JSONException {
//                switch (response.getInt("status_code")) {
//                    case 200:
//                        onAddPostSuccess();
//                        break;
//
//                }
//            }
//
//            @Override
//            public void handleError(Exception e) {
//                e.printStackTrace();
//
//            }
//        });
//    }


}
