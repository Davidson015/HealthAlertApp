package com.eproject.healthalert;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    ListView l;
    String[] appointment_description
            = { "Appointments 1 , 22 quarry Rd , Ibaara , Abeokuta",
                "Appointments 2 , Cultural Center , Kuto , Abeokuta",
                "Appointments 3 , Obansanjo Library , Abeokuta"};

    String usernameVal;
    TextView username;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
//        getSupportActionBar().setTitle("Schedule");

        // Initializing drawer layout and actionbarToggle
        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initializing NavigationView
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        usernameVal = getIntent().getStringExtra("username");
        username = findViewById(R.id.username);
        username.setText(String.format("Hello %s", usernameVal));

        l = findViewById(R.id.appointment_list_view);

        ArrayAdapter<String> arr = new ArrayAdapter<String>(
                this , com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
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
//        switch (menuItem.getItemId()) {
//            case R.id.home:
//                intent = new Intent(ScheduleActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.schedules:
//                drawer.closeDrawers();
//                break;
//            case R.id.settings:
//                intent = new Intent(ScheduleActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.about:
//                intent = new Intent(ScheduleActivity.this, AboutActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.exit_app:
//                // Confirming if the user wants to exit the app
//                confirmExit();
//                break;
//            default:
//                break;
//        }

//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());
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
}