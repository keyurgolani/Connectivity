package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.LoginActivity;

/**
 * Created by gauravchodwadia on 5/6/17.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: check if the user is logged in or not

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
