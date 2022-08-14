package com.eproject.healthalert;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eproject.healthalert.adapter.MedicineDosageAdapter;
import com.eproject.healthalert.model.MedicineDosage;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MedicineDosageActivity extends AppCompatActivity {
    Intent intent;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private long pressedTime;

    private RecyclerView dosage_recyclerView;
    private ArrayList<MedicineDosage> dosageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_dosage);


        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportActionBar().setTitle("Medicine Dosage");

        // Initializing drawer layout and actionbarToggle
        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initializing NavigationView
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

//        Setting Recycler View for dosage cards
        dosage_recyclerView = findViewById(R.id.recyclerview);

        dosageArrayList = new ArrayList<>();

        dosageArrayList.add(new MedicineDosage("Fl1022", "Fluoride", "2 times daily", "10 hours interval", "10/08/2022", "20/08/2022", "2"));
        dosageArrayList.add(new MedicineDosage("cit0056", "Vitamins", "3 times daily", "Any time", "anytime", "anytime", "1"));
        dosageArrayList.add(new MedicineDosage("Ib003", "Ibruprofen", "2 times daily", "Morning and evening", "14/08/2022", "16/08/2022", "2"));
        dosageArrayList.add(new MedicineDosage("Chlr009", "Chloroquine", "2 times daily", "8 hours Interval", "15/09/2022", "17/09/2022", "1"));
        dosageArrayList.add(new MedicineDosage("aplne009", "Amplodipine", "Once  daily", "Morning and evening", "5/09/2022", "17/09/2022", "1"));
        dosageArrayList.add(new MedicineDosage("emz0098", "Emzor Paracetamol", "2 times daily", "Morning and evening", "anytime", "anytime", "2 "));


        MedicineDosageAdapter dosageAdapter = new MedicineDosageAdapter(this, dosageArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        dosage_recyclerView.setLayoutManager(linearLayoutManager);
        dosage_recyclerView.setAdapter(dosageAdapter);

        Button addbtn = findViewById(R.id.add_dosage_btn);

        addbtn.setOnClickListener(v -> {
            intent = new Intent(MedicineDosageActivity.this, AddDosageActivity.class);
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

            case R.id.medicine:
                drawer.closeDrawers();
                break;
            case R.id.appointments:
                intent = new Intent(MedicineDosageActivity.this, AppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
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