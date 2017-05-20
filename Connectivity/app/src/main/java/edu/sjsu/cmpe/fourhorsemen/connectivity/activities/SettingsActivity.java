package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;

public class SettingsActivity extends AppCompatActivity {

    private static String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Alert message to be shown");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Capture",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Pick",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                            }
                        });
                alertDialog.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap bitmap = null;
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette palette = Palette.from(bitmap).generate();
            int default_color = 0x000000;
            int vibrant = palette.getVibrantColor(default_color);
            int vibrantLight = palette.getLightVibrantColor(default_color);
            int vibrantDark = palette.getDarkVibrantColor(default_color);
            int muted = palette.getMutedColor(default_color);
            int mutedLight = palette.getLightMutedColor(default_color);
            int mutedDark = palette.getDarkMutedColor(default_color);
            Log.d(TAG, String.format("0x%08X", default_color) + "\n" +
                    String.format("0x%08X", vibrant) + "\n" +
                    String.format("0x%08X", vibrantLight) + "\n" +
                    String.format("0x%08X", vibrantDark) + "\n" +
                    String.format("0x%08X", muted) + "\n" +
                    String.format("0x%08X", mutedLight) + "\n" +
                    String.format("0x%08X", mutedDark));
        }
    }

}
