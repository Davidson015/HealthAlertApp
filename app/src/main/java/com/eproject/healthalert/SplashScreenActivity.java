package com.eproject.healthalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 3000;
//    After completion of 2000 ms, the next activity will get started.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This method is used so that your splash activity
        //can cover the entire screen.

        // Hiding the Action Bar
        setContentView(R.layout.activity_splash_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this,
                    MainActivity.class);
            //Intent is used to switch from one activity to another.

            startActivity(i);
            //invoke the SecondActivity.

            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }
}