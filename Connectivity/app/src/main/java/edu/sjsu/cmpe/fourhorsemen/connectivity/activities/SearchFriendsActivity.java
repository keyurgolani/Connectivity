package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnKeyListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Post;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Profile;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.MyPostRecyclerViewAdapter;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.MySearchedFriendsAdapter;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import android.view.MotionEvent;


public class SearchFriendsActivity extends AppCompatActivity {

    private static String TAG = SearchFriendsActivity.class.getSimpleName();

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    private PostFragment.OnListFragmentInteractionListener mListener;
    @Bind(R.id.search_email) EditText searchEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        recyclerView=new RecyclerView(this);

        // Set a key listener callback so that users can search by pressing "Enter"
        searchEmail.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER ) {
                    if( event.getAction() == KeyEvent.ACTION_DOWN ) {
                        searchFor(getBaseContext());
                    }
                    return true;
                }
                return false;
            }
        });

        searchEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchEmail.getRight() - searchEmail.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //TODO: Search Code here
                        return true;
                    }
                }
                return false;
            }
        });

        // Set the adapter

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MySearchedFriendsAdapter(searchFor(this),mListener);
        recyclerView.setAdapter(mAdapter);
    }


    private List<Profile> searchFor(Context context){
        final List<Profile> profile_list = new ArrayList<Profile>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("unique_id", PreferenceHandler.getAccessKey());
        params.put("email", searchEmail.getText().toString());


        RequestHandler.HTTPRequest(context, ProjectProperties.METHOD_GET_PROFILE_FROM_EMAIL, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(response.getInt("status_code") == 200) {
                    JSONArray profile = response.getJSONArray("message");
                    JSONObject currentObj = profile.getJSONObject(0);
                    Log.d("search result", currentObj.toString());
                    Profile prof_object = new Profile();
//                    prof_object
//                    profile_list.add(prof_object);


                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "No Profile Found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                e.printStackTrace();
            }
        });

        return profile_list;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    private List<Post> getSearchedFriends(Context context) {
        final List<Post> personalTimeline = new ArrayList<Post>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("unique_id", PreferenceHandler.getAccessKey());


        RequestHandler.HTTPRequest(context, ProjectProperties.METHOD_FETCH_TIMELINE, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(response.getInt("status_code") == 200) {
                    JSONArray posts = response.getJSONArray("message");
                    for(int i  = 0; i < posts.length(); i++) {
                        JSONObject currentObj = posts.getJSONObject(i);
                        personalTimeline.add(new Post(currentObj.getInt("post_id"), currentObj.getString("photo"), currentObj.getString("post"), new Profile(currentObj.getInt("profile"), currentObj.getString("profile_pic"), currentObj.getString("screen_name")), currentObj.getString("timestamp")));
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "Internal Error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                e.printStackTrace();
            }
        });
        return personalTimeline;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (getBaseContext() instanceof PostFragment.OnListFragmentInteractionListener) {
            mListener = (PostFragment.OnListFragmentInteractionListener) getBaseContext();
        } else {
            throw new RuntimeException(getBaseContext().toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
}
