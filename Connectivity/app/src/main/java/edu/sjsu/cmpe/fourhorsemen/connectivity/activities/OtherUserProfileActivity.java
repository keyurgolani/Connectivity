package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;

public class OtherUserProfileActivity extends AppCompatActivity {

    private static String TAG = OtherUserProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
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
}
