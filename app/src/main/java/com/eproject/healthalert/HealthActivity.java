package com.eproject.healthalert;

import static android.content.ContentValues.TAG;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.eproject.healthalert.model.PersonalHealthVitals;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HealthActivity extends AppCompatActivity {
    Intent intent;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    Button addvitalsbtn;
    NavigationView navigationView;

    ImageView themeToggle;

    // Creating value views
    TextView heartRate, bp, temperature, bloodSugar, weight, height, respiratoryRate;

    String userEmail;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    // Creating shared preferences
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        // Initializing views
        heartRate = findViewById(R.id.heart_rate_val);
        bp = findViewById(R.id.bp_val);
        temperature = findViewById(R.id.body_temp_val);
        bloodSugar = findViewById(R.id.blood_sugar_val);
        height = findViewById(R.id.height_val);
        weight = findViewById(R.id.weight_val);
        respiratoryRate = findViewById(R.id.resp_rate_val);

        // Getting the userEmail from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        userEmail = pref.getString("email", "");

        // Getting User health vitals from firebase db
        database.getReference("healthVitals").orderByChild("userId").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // Getting the healthVitals values
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        PersonalHealthVitals healthVitals = childSnapshot.getValue(PersonalHealthVitals.class);

                        // Setting the healthVitals values
                        heartRate.setText(healthVitals.getPulseRate() + " bpm");
                        bp.setText(healthVitals.getBloodPressure() + " mmHg");
                        temperature.setText(healthVitals.getBodyTemperature() + " °C");
                        bloodSugar.setText(healthVitals.getBloodGlucose() + " mg/dL");
                        respiratoryRate.setText(healthVitals.getRespiratoryRate() + " per min");
                        height.setText(healthVitals.getHeight() + " inches");
                        weight.setText(healthVitals.getWeight() + " kg");

                        if (healthVitals.getPulseRate().equals("")) {
                            heartRate.setText("N/A");
                        } if (healthVitals.getBloodPressure().equals("")) {
                            bp.setText("N/A");
                        } if (healthVitals.getBodyTemperature().equals("")) {
                            temperature.setText("N/A");
                        } if (healthVitals.getBloodGlucose().equals("")) {
                            bloodSugar.setText("N/A");
                        } if (healthVitals.getRespiratoryRate().equals("")) {
                            respiratoryRate.setText("N/A");
                        } if (healthVitals.getHeight().equals("")) {
                            height.setText("N/A");
                        } if (healthVitals.getWeight().equals("")) {
                            weight.setText("N/A");
                        }
                    }
                } else {
                    Toast.makeText(HealthActivity.this, "No health vitals added yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        addvitalsbtn = findViewById(R.id.add_vitals_btn);

        addvitalsbtn.setOnClickListener(v -> {
            intent = new Intent(HealthActivity.this, UpdateHealthVitalsActivity.class);
            startActivity(intent);
        });

        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportActionBar().setTitle("Health Vitals");

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
                intent = new Intent(com.eproject.healthalert.HealthActivity.this, com.eproject.healthalert.AppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.health_vitals:
                drawer.closeDrawers();
                break;
            case R.id.medicine:
                intent = new Intent(HealthActivity.this, MedicineDosageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.feedback:
                intent = new Intent(HealthActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.contact:
                intent = new Intent(HealthActivity.this, ContactActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.help:
                intent = new Intent(HealthActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.settings:
                intent = new Intent(HealthActivity.this, SettingsActivity.class);
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
            Intent intent = new Intent(HealthActivity.this, MainActivity.class);

            // Displaying a Toast message
            Toast.makeText(HealthActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

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