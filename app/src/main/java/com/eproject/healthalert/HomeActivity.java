package com.eproject.healthalert;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    ListView l;
    String[] appointment_description = new String[5];

    String usernameVal;
    TextView username;
    DrawerLayout drawer;
    ImageView userprofilepic, themeToggle;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    private long pressedTime;
    SharedPreferences pref;
    CardView appointmentCard, medicalDosage, healthVitals, fidgetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get the VIBRATOR_SERVICE system service
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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

        userprofilepic = findViewById(R.id.user_profile_pic);
        appointmentCard = findViewById(R.id.appointments_card_view);
        medicalDosage = findViewById(R.id.medicine_dosage_card_view);
        healthVitals = findViewById(R.id.health_vitals_card_view);
        fidgetBtn = findViewById(R.id.fidget_btn);

        // Adding OnClickListener to the Fidget Button
        fidgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Making the Fidget Button set off device vibration
                final VibrationEffect vibrationEffect;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

                    // create vibrator effect with the constant EFFECT_CLICK
                    vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);

                    // it is safe to cancel other vibrations currently taking place
                    vibrator.cancel();

                    vibrator.vibrate(vibrationEffect);
                }
            }
        });

        // Getting the username from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        usernameVal = pref.getString("username", "");
        username = findViewById(R.id.username);
        // Setting the username to the TextView
        username.setText(String.format("Hello %s", usernameVal));

//        // Initializing the ListView
//        l = findViewById(R.id.upcoming_appts_list);
//
//        // Getting the appointment description from the SharedPreferences
//        SharedPreferences pref = getSharedPreferences("appointment", MODE_PRIVATE);
//
//        // Adding apptDesc to the appointment_description array
//        for (int i = 0; i < 5; i++) {
//            String apptDesc = pref.getString("appointmentDescription" + i, "");
//            appointment_description[i] = apptDesc;
//        }
//
//        ArrayAdapter<String> arr = new ArrayAdapter<>(
//                this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
//                appointment_description
//        );
//        l.setAdapter(arr);

        // Adding onClickListener to the User Profile Picture
        userprofilepic.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Adding onClickListener to the CardViews
        // Appointment CardView
        appointmentCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AppointmentActivity.class);
            startActivity(intent);
        });
        // Medical Dosage CardView
        medicalDosage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MedicineDosageActivity.class);
            startActivity(intent);
        });
        // Health Vitals CardView
        healthVitals.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HealthActivity.class);
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
                drawer.closeDrawers();
                break;
            case R.id.appointments:
                intent = new Intent(HomeActivity.this, AppointmentActivity.class);
                startActivity(intent);
                break;
            case R.id.health_vitals:
                intent = new Intent(HomeActivity.this, HealthActivity.class);
                startActivity(intent);
                break;
            case R.id.medicine:
                intent = new Intent(HomeActivity.this, MedicineDosageActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback:
                intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.contact:
                intent = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                intent = new Intent(HomeActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                // confirmation dialog
                confirmLogout();
                break;
            default:
                break;
        }
        drawer.closeDrawers();
    }

    // Creating the onBackPressed method to handle the back button press
    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            // Confirming if the user wants to logout
            confirmLogout();
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
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);

            // Displaying a Toast message
            Toast.makeText(HomeActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

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

    public boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}