package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import android.view.MotionEvent;


public class SearchFriendsActivity extends AppCompatActivity {

    private static String TAG = SearchFriendsActivity.class.getSimpleName();

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    @Bind(R.id.search_email) EditText searchEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);


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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mAdapter = new MyPostRecyclerViewAdapter(getPersonalTimeline(getContext()), mListener);
        recyclerView.setAdapter(mAdapter);

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


    private List<Post> getSearchedFriends(final Context context) {
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
                    Toast.makeText(context, "Internal Error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                e.printStackTrace();
            }
        });
        return personalTimeline;
    }

}
