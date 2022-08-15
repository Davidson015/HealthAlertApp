package com.eproject.healthalert;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eproject.healthalert.adapter.AppointmentAdapter;
import com.eproject.healthalert.model.Appointment;
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
        getSupportActionBar().setTitle("Personal Health Vitals");

        // Initializing drawer layout and actionbarToggle
        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initializing NavigationView
        navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

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
//            case R.id.settings:
//                intent = new Intent(HealthActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.logout:
                // Redirecting User to MainActivity
                intent = new Intent(com.eproject.healthalert.HealthActivity.this, MainActivity.class);

                // Displaying a Toast message
                Toast.makeText(com.eproject.healthalert.HealthActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

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