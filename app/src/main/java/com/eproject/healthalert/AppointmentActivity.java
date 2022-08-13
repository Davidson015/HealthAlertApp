package com.eproject.healthalert;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.eproject.healthalert.adapter.AppointmentAdapter;
import com.eproject.healthalert.model.Appointment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {

    CardView card;
    Intent intent;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private long pressedTime;

    private RecyclerView appointment_recyclerView;
    private ArrayList<Appointment> appointmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Button addbtn = findViewById(R.id.add_appointment_btn);

        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        getSupportActionBar().setTitle("");

        // Initializing drawer layout and actionbarToggle
        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initializing NavigationView
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

//        Setting Recycler View for Appointment cards
        appointment_recyclerView = findViewById(R.id.recyclerview);

        appointmentArrayList = new ArrayList<>();

        appointmentArrayList.add(new Appointment("","","Dr Adam Smith ,Dentist Meeting","21st Aug 2022","10:00 am","New City Clinic",true));
        appointmentArrayList.add(new Appointment("","","Dr Adam Smith ,Dentist Meeting","21st Aug 2022","10:00 am","New City Clinic",true));
        appointmentArrayList.add(new Appointment("","","Dr Adam Smith ,Dentist Meeting","21st Aug 2022","10:00 am","New City Clinic",true));
        appointmentArrayList.add(new Appointment("",""," Dr Angel Smith,Gynecologist","23rd Sep 2022","08:00 am","Lagoon Hospital",true));
        appointmentArrayList.add(new Appointment("",""," Dr Angel Smith,Gynecologist Checkup","23rd Sep 2022","08:00 am","Lagoon Hospital",false));
        appointmentArrayList.add(new Appointment("","","Dr Beau Hightower , Chiropractic Adjustment ","21st Aug 2022","10:00 am","New City Clinic",true));


        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this,appointmentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        appointment_recyclerView.setLayoutManager(linearLayoutManager);
        appointment_recyclerView.setAdapter(appointmentAdapter);

        // make card checkable
        addbtn.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentActivity.this, AddAppointmentActivity.class);
            startActivity(intent);
        });

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
                drawer.closeDrawers();
                break;
//            case R.id.settings:
//                intent = new Intent(ScheduleActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.health_vitals:
//                intent = new Intent(ScheduleActivity.this, AboutActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.medicine:
//                intent = new Intent(ScheduleActivity.this, AboutActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.exit_app:
//                // Confirming if the user wants to exit the app
//                confirmExit();
//                break;
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
}