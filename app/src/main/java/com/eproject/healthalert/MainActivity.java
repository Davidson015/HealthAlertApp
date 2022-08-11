package com.eproject.healthalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
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

    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}