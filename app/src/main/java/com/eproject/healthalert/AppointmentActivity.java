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
import android.content.SharedPreferences;
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

    // AppointmentId
    private int apptId = 0;

    CardView card;
    Intent intent;
    DrawerLayout drawer;
    Button addbtn, editbtn;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private long pressedTime;
    SharedPreferences pref;

    private RecyclerView appointment_recyclerView;
    private ArrayList<Appointment> appointmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        addbtn = findViewById(R.id.add_appointment_btn);
        editbtn = findViewById(R.id.edit_btn);

        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportActionBar().setTitle("Appointments");

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

        appointmentArrayList.add(new Appointment("", apptId++, "Dr Adam Smith ,Dentist Meeting", "21st Aug 2022", "10:00 am", "New City Clinic"));
        appointmentArrayList.add(new Appointment("", apptId++, "Dr Adam Smith ,Dentist Meeting", "21st Aug 2022", "10:00 am", "New City Clinic"));
        appointmentArrayList.add(new Appointment("", apptId++, "Dr Adam Smith ,Dentist Meeting", "21st Aug 2022", "10:00 am", "New City Clinic"));
        appointmentArrayList.add(new Appointment("", apptId++, "Dr Angel Smith,Gynecologist", "23rd Sep 2022", "08:00 am", "Lagoon Hospital"));
        appointmentArrayList.add(new Appointment("", apptId++, "Dr Angel Smith,Gynecologist Checkup", "23rd Sep 2022", "08:00 am", "Lagoon Hospital"));
        appointmentArrayList.add(new Appointment("", apptId++, "Dr Beau Hightower , Chiropractic Adjustment ", "21st Aug 2022", "10:00 am", "New City Clinic"));


        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this, appointmentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        appointment_recyclerView.setLayoutManager(linearLayoutManager);
        appointment_recyclerView.setAdapter(appointmentAdapter);


        addbtn.setOnClickListener(v -> {
            intent = new Intent(AppointmentActivity.this, AddAppointmentActivity.class);
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
            case R.id.home:
                intent = new Intent(AppointmentActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.appointments:
                intent = new Intent(AppointmentActivity.this, AppointmentActivity.class);
                drawer.closeDrawers();
                break;
//            case R.id.health_vitals:
//                intent = new Intent(AppointmentActivity.this, HealthVitals.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.medicine:
                intent = new Intent(AppointmentActivity.this, MedicineDosageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.feedback:
                intent = new Intent(AppointmentActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.contact:
//                intent = new Intent(AppointmentActivity.this, ContactActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.help:
//                intent = new Intent(AppointmentActivity.this, HelpActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.settings:
//                intent = new Intent(AppointmentActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.logout:
                // Redirecting User to MainActivity
                intent = new Intent(AppointmentActivity.this, MainActivity.class);

                // Displaying a Toast message
                Toast.makeText(AppointmentActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finishAffinity();

                // Clearing the SharedPreferences
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();

                break;
            default:
                break;
        }
        drawer.closeDrawers();
    }

    // Overriding the onBackPressed method
    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
            finish();
        }
    }
}