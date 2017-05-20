package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.Manifest;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;

/**
 * Created by keyurgolani on 5/15/17.
 */

public class AppIntro extends IntroActivity {
    // Use this link for reference on more features
    // https://github.com/heinrichreimer/material-intro
    @Override protected void onCreate(Bundle savedInstanceState){
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        addSlide(new SimpleSlide.Builder()
                .title("ConnActivity")
                .description("The Social Media App That is just PERFECT!")
                .image(R.drawable.socialmedia)
                .background(R.color.colorPrimaryLight)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("ConnActivity")
                .description("The Social Media App That is just PERFECT!")
                .image(R.drawable.socialmedia)
                .background(R.color.colorPrimaryLight)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("ConnActivity")
                .description("The Social Media App That is just PERFECT!")
                .image(R.drawable.socialmedia)
                .background(R.color.colorPrimaryLight)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("ConnActivity")
                .description("The Social Media App That is just PERFECT!")
                .image(R.drawable.socialmedia)
                .background(R.color.colorPrimaryLight)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        setButtonBackFunction(BUTTON_BACK_FUNCTION_SKIP);
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);
        setPageScrollDuration(100);
    }

    @Override
    public void onBackPressed() {
        PreferenceHandler.clearFirstLaunch();
        moveTaskToBack(true);
    }

}