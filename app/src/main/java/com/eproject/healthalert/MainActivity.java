package com.eproject.healthalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.eproject.healthalert.adapter.IntroViewAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    WormDotsIndicator dots;
    IntroViewAdapter introViewAdapter;
    Integer[] images={R.drawable.ic_doctor,R.drawable.ic_vitals,R.drawable.ic_drugs,R.drawable.ic_appointment};
    Integer[] titles={R.string.dd1,R.string.dd2,R.string.dd3,R.string.dd4};
    Integer[] descriptions={R.string.df1,R.string.df2,R.string.df3,R.string.df4};
    Timer timer = new Timer();

    Button register_btn;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Making the Status Transparent
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // Checking if the device is running on a version of Android that supports dark mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Checking if the device is in dark mode
            if (!isNightMode(this)) {
                // Setting the status bar color to dark
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        viewPager = findViewById(R.id.view_pager);
        dots = findViewById(R.id.dots_indicator);

        introViewAdapter = new IntroViewAdapter(this, images, titles, descriptions);
        viewPager.setAdapter(introViewAdapter);
        dots.attachTo(viewPager);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){

                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%images.length);
                    }
                });
            }
        };
        timer.schedule(timerTask, 4000, 4000);

        // Initialize Navigation buttons
        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);

        // Register button
        register_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Login button
        login_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    // Creating the setWindowFlag method
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    // Creating the isNightMode method
    public boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}