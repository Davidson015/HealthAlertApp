package com.eproject.healthalert;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.eproject.healthalert.adapter.AppointmentAdapter;
import com.eproject.healthalert.model.Appointment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {

    // AppointmentId
    private int apptId = 0;

    CardView card;
    Intent intent;
    DrawerLayout drawer;
    Button addbtn, editbtn, deletebtn;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    ShimmerFrameLayout shimmer;

    ImageView themeToggle;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    SharedPreferences pref;

    private RecyclerView appointmentRecyclerView;
    private ArrayList<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        addbtn = findViewById(R.id.add_appointment_btn);
        editbtn = findViewById(R.id.edit_btn);
        deletebtn = findViewById(R.id.delete_btn);

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

        View headerView = navigationView.getHeaderView(0);

        themeToggle = headerView.findViewById(R.id.theme_toggle);

        // Setting the src of the theme toggle imageview in respect to the devices theme
        if (isNightMode(this)) {
            themeToggle.setImageResource(R.drawable.ic_light);
        } else {
            themeToggle.setImageResource(R.drawable.ic_dark);
        }

        // Adding onClickListener to the Theme Toggle
        themeToggle.setOnClickListener(v -> {
            // checking if the device is on dark mode and setting the themeToggle function respectively
            if (!isNightMode(this)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                themeToggle.setImageResource(R.drawable.ic_dark);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                themeToggle.setImageResource(R.drawable.ic_light);
            }
        });

//        Setting Recycler View for Appointment cards
        appointmentRecyclerView = findViewById(R.id.recyclerview);

        // Getting Shimmer from the layout (Initializing it)
        shimmer = findViewById(R.id.shimmer_view_container);

        // Starting Shimmer Animation
        shimmer.startShimmer();

        // Setting the RecylcerView Visibility to Gone
        appointmentRecyclerView.setVisibility(View.GONE);

        // initialing arraylist for appointment
        appointmentList = new ArrayList<>();

        // Getting user email from shared preferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        String userEmail = pref.getString("email", "");

        // Getting appointments from firebase db
        database.getReference("appointments").orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // Getting the appointment values
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Appointment appointment = childSnapshot.getValue(Appointment.class);
                        appointmentList.add(appointment);
                    }
                    // Setting the adapter for the recycler view
                    AppointmentAdapter appointmentAdapter = new AppointmentAdapter(AppointmentActivity.this, appointmentList);
                    appointmentRecyclerView.setAdapter(appointmentAdapter);

                    // Stopping the Shimmer Animation
                    shimmer.stopShimmer();

                    // Setting the view of the shimmer to gone
                    shimmer.setVisibility(View.GONE);

                    // Setting the view of the recyclerView to visible
                    appointmentRecyclerView.setVisibility(View.VISIBLE);

                    appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(AppointmentActivity.this));
                } else {

                    // Stopping the Shimmer Animation
                    shimmer.stopShimmer();

                    // Setting the view of the shimmer to gone
                    shimmer.setVisibility(View.GONE);

                    // Showing toast message that there's no appointments
                    Toast.makeText(AppointmentActivity.this, "No Appointments to show.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        // Adding click listener to Delete button
//        if (deletebtn != null) {
//            deletebtn.setOnClickListener(v -> {
//                // Getting appointment Id of the clicked appointment
//                int clickedapptId = appointmentList.get(appointmentRecyclerView.getChildAdapterPosition(v)).getAppointmentId();
//                // Getting the appointment id
//                database.getReference("appointments").orderByChild("appointmentId").equalTo(clickedapptId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Appointment appointment = snapshot.getValue(Appointment.class);
//                        System.out.println(appointment.getAppointmentDescription());
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Getting Post failed, log a message
//                        Log.w(TAG, "loadPost:onCancelled", error.toException());
//                    }
//                });
//                startActivity(intent);
//            });
//        }

        // Adding click listener to add appointment button
        addbtn.setOnClickListener(v -> {
            intent = new Intent(AppointmentActivity.this, AddAppointmentActivity.class);
            startActivity(intent);
            finish();
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
                finish();
                break;
            case R.id.appointments:
                drawer.closeDrawers();
                break;
            case R.id.health_vitals:
                intent = new Intent(AppointmentActivity.this, HealthActivity.class);
                startActivity(intent);
                finish();
                break;
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
            case R.id.contact:
                intent = new Intent(AppointmentActivity.this, ContactActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.help:
                intent = new Intent(AppointmentActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.settings:
                intent = new Intent(AppointmentActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Logging out the user
                confirmLogout();
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

    // Creating the confirmLogout method to confirm if the user wants to exit the app
    private void confirmLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("You're about to logout. Are you sure?");
        // Setting the positive button to exit the app
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Redirecting User to MainActivity
            Intent intent = new Intent(AppointmentActivity.this, MainActivity.class);

            // Displaying a Toast message
            Toast.makeText(AppointmentActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

            finishAffinity();
            startActivity(intent);

            // Clearing the SharedPreferences
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
        });
        // Setting the negative button to cancel the exit
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}