package com.eproject.healthalert;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eproject.healthalert.adapter.AppointmentAdapter;
import com.eproject.healthalert.adapter.MedicineDosageAdapter;
import com.eproject.healthalert.model.Appointment;
import com.eproject.healthalert.model.MedicineDosage;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedicineDosageActivity extends AppCompatActivity {
    Intent intent;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    ShimmerFrameLayout shimmer;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    SharedPreferences pref;

    private RecyclerView dosageRecyclerView;
    private ArrayList<MedicineDosage> dosageList;

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
        dosageRecyclerView = findViewById(R.id.recyclerview);

        // Getting Shimmer from the layout (Initializing it)
        shimmer = findViewById(R.id.shimmer_view_container);

        // Starting Shimmer Animation
        shimmer.startShimmer();

        // Setting the RecylcerView Visibility to Gone
        dosageRecyclerView.setVisibility(View.GONE);

        dosageList = new ArrayList<>();

        // Getting user email from shared preferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        String userEmail = pref.getString("email", "");

        // Getting dosages from firebase db
        database.getReference("dosages").orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // Getting the appointment values
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        MedicineDosage appointment = childSnapshot.getValue(MedicineDosage.class);
                        dosageList.add(appointment);
                    }
                    // Setting the adapter for the recycler view
                    MedicineDosageAdapter appointmentAdapter = new MedicineDosageAdapter(MedicineDosageActivity.this, dosageList);
                    dosageRecyclerView.setAdapter(appointmentAdapter);

                    // Stopping the Shimmer Animation
                    shimmer.stopShimmer();

                    // Setting the view of the shimmer to gone
                    shimmer.setVisibility(View.GONE);

                    // Setting the view of the recyclerView to visible
                    dosageRecyclerView.setVisibility(View.VISIBLE);

                    dosageRecyclerView.setLayoutManager(new LinearLayoutManager(MedicineDosageActivity.this));
                } else {

                    // Stopping the Shimmer Animation
                    shimmer.stopShimmer();

                    // Setting the view of the shimmer to gone
                    shimmer.setVisibility(View.GONE);

                    // Showing toast message
                    Toast.makeText(MedicineDosageActivity.this, "No Dosages to show.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        Button addbtn = findViewById(R.id.add_dosage_btn);

        addbtn.setOnClickListener(v -> {
            intent = new Intent(MedicineDosageActivity.this, AddDosageActivity.class);
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
                intent = new Intent(MedicineDosageActivity.this, AppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.health_vitals:
                intent = new Intent(MedicineDosageActivity.this, HealthActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.medicine:
                drawer.closeDrawers();
                break;
            case R.id.feedback:
                intent = new Intent(MedicineDosageActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.contact:
                intent = new Intent(MedicineDosageActivity.this, ContactActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.help:
                intent = new Intent(MedicineDosageActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.settings:
                intent = new Intent(MedicineDosageActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // confirmation Dialog
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
            Intent intent = new Intent(MedicineDosageActivity.this, MainActivity.class);

            // Displaying a Toast message
            Toast.makeText(MedicineDosageActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

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
}