package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.content.Intent;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements PostFragment.OnListFragmentInteractionListener {

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
                    selectedFragment = PostFragment.newInstance(1);
                    break;
                case R.id.navigation_chat:
                    selectedFragment = PostFragment.newInstance(1);
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

        //TODO: check if the user is logged in or not
        boolean isLoggedIn = true;
        if(!isLoggedIn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
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
    public void onListFragmentInteraction(DummyContent.DummyPost item) {

    }
}
