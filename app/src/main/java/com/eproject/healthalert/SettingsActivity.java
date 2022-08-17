package com.eproject.healthalert;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.eproject.healthalert.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {
    Intent intent;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    String userEmail;

    ImageView themeToggle;

    // Creating User object
    User user;

    // User value from db
    User dbUser;

    // Creating form fields
    TextInputEditText firstName, lastName, email, age, phoneNumber, password, confirmPassword;
    Button saveBtn, deleteBtn;

    // Creating Shared Preferences
    SharedPreferences pref;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Getting the userEmail from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        userEmail = pref.getString("email", "");

        // Initializing form fields
        firstName = findViewById(R.id.edit_first_name);
        lastName = findViewById(R.id.edit_last_name);
        email = findViewById(R.id.edit_email);
        age = findViewById(R.id.edit_age);
        phoneNumber = findViewById(R.id.edit_phone_no);
        password = findViewById(R.id.edit_password);
        confirmPassword = findViewById(R.id.edit_con_password);
        saveBtn = findViewById(R.id.edit_profile_btn);
        deleteBtn = findViewById(R.id.del_profile_btn);

        // Getting user from the database
        database.getReference("users").orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if the data snapshot is null or not and performing specific actions
                if (snapshot.getValue() != null) {
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                        dbUser = childSnapShot.getValue(User.class);

                        // Setting fields to default values
                        firstName.setText(dbUser.getFirstName());
                        lastName.setText(dbUser.getLastName());
                        email.setText(dbUser.getEmail());
                        age.setText(dbUser.getAge());
                        phoneNumber.setText(dbUser.getPhoneNumber());
                        password.setText(dbUser.getPassword());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        // Adding click listener to the save button
        saveBtn.setOnClickListener(v -> {

            // Checking if all fields are Empty
            if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || age.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                if (firstName.getText().toString().isEmpty()) {
                    firstName.setError("Field cannot be empty!");
                }
                if (lastName.getText().toString().isEmpty()) {
                    lastName.setError("Field cannot be empty!");
                }
                if (email.getText().toString().isEmpty()) {
                    email.setError("Field cannot be empty!");
                }
                if (age.getText().toString().isEmpty()) {
                    age.setError("Field cannot be empty!");
                }
                if (phoneNumber.getText().toString().isEmpty()) {
                    phoneNumber.setError("Field cannot be empty!");
                }
            }

            // Checking if the confirm password field is empty
            else if (confirmPassword.getText().toString().isEmpty()) {
                // showing toast to tell user to confirm password
                Toast.makeText(this, "Please confirm password!", Toast.LENGTH_SHORT).show();
            }

            // Validating email regex
            else if (!email.getText().toString().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                Toast.makeText(this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
                email.setError("Invalid Email Address!");
            }

            // Checking if age is a number
            else if (!age.getText().toString().matches("[0-9]+")) {
                Toast.makeText(this, "Invalid Age!", Toast.LENGTH_SHORT).show();
                age.setError("Invalid Age!");
            }

            // Checking password length
            else if (password.getText().toString().length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
                password.setError("Password must be at least 6 characters long!");
            } else {
                // Setting up User
                user = new User(firstName.getText().toString().trim(), lastName.getText().toString().trim(), email.getText().toString().trim(), age.getText().toString(), dbUser.getGender(), password.getText().toString(), phoneNumber.getText().toString().trim());

                // creating a user reference(UserId)
                String userId = user.getEmail().replace("@", "_").replace(".", "_") + "-000";

                // Checking if the user is already registered
                database.getReference("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(SettingsActivity.this, "There's an error on our end, Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Updating the user in the database
                            database.getReference("users").child(userId).setValue(user);

                            // Creating Toast to show success
                            Toast.makeText(SettingsActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                            // Updating the SharedPreferences
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", user.getEmail());
                            editor.putString("username", user.getFirstName());

                            // Applying editor changes to SharedPreferences
                            editor.apply();

                            // Redirecting to the HomeActivity
                            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                });
            }
        });

        // Adding click listener to the delete button to delete the user from the database
        deleteBtn.setOnClickListener(v -> {
            // Creating a dialog to confirm the deletion
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Delete Profile");
            builder.setMessage("Are you sure you want to delete your profile?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Creating a user reference(UserId)
                String userId = dbUser.getEmail().replace("@", "_").replace(".", "_") + "-000";
                database.getReference("users").child(userId).removeValue();
                Toast.makeText(SettingsActivity.this, "Profile deleted successfully!", Toast.LENGTH_SHORT).show();

                // Redirecting to the LoginActivity
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }).setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            }).show();
        });

        // Navigation Drawer
        // Initializing Toolbar and setting it as the actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportActionBar().setTitle("Profile Settings");

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
                intent = new Intent(SettingsActivity.this, com.eproject.healthalert.AppointmentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.health_vitals:
                intent = new Intent(SettingsActivity.this, HealthActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.medicine:
                intent = new Intent(SettingsActivity.this, MedicineDosageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.feedback:
                intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.contact:
                intent = new Intent(SettingsActivity.this, ContactActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.help:
                intent = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.settings:
                drawer.closeDrawers();
                break;
            case R.id.logout:
                // Confirmation Dialog
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
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

            // Displaying a Toast message
            Toast.makeText(SettingsActivity.this, "See you soon!", Toast.LENGTH_SHORT).show();

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