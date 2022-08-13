package com.eproject.healthalert;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    ListView l;
    String[] appointment_description
            = { "22 quarry Rd , Ibaara , Abeokuta",
                "Cultural Center , Kuto , Abeokuta",
                "Obansanjo Library , Abeokuta"};

    String usernameVal;
    TextView username;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private long pressedTime;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportActionBar().setTitle("");

        // Initializing drawer layout and actionbarToggle
        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initializing NavigationView
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        // Getting the username from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        usernameVal = pref.getString("username", "");
        username = findViewById(R.id.username);
        // Setting the username to the TextView
        username.setText(String.format("Hello %s", usernameVal));

        l = findViewById(R.id.upcoming_appts_list);

        ArrayAdapter<String> arr = new ArrayAdapter<>(
                this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                appointment_description
        );
        l.setAdapter(arr);

    }

    // Creating the setUpDrawerContent method
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    // Creating the selectDrawerItem method to handle navigation item clicks
    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.appointments:
                intent = new Intent(HomeActivity.this, AppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.health_vitals:
//                intent = new Intent(HomeActivity.this, HealthVitals.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.medicine:
//                intent = new Intent(HomeActivity.this, MedicineActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.feedback:
//                intent = new Intent(HomeActivity.this, FeedbackActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.contact:
//                intent = new Intent(HomeActivity.this, ContactActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.help:
//                intent = new Intent(HomeActivity.this, HelpActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.settings:
//                intent = new Intent(HomeActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.logout:
                // Redirecting User to MainActivity
                intent = new Intent(HomeActivity.this, MainActivity.class);

                // Displaying a Toast message
                Toast.makeText(HomeActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finishAffinity();

                // Clearing the SharedPreferences
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                break;
            default:
                break;
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }

    // Creating the onBackPressed method to handle the back button press
    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
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

//    public boolean isNightMode(Context context) {
//        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
//    }
}