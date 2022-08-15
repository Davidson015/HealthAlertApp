package com.eproject.healthalert;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.eproject.healthalert.adapter.AppointmentAdapter;
import com.eproject.healthalert.model.Appointment;
import com.eproject.healthalert.model.User;
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
    private long pressedTime;

    String username;

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

//        Setting Recycler View for Appointment cards
        appointmentRecyclerView = findViewById(R.id.recyclerview);

        // initialing arraylist for appointment
        appointmentList = new ArrayList<>();

        // Getting user email from shared preferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        String userEmail = pref.getString("email", "");

        // Getting username from shared preferences
        username = pref.getString("username", "");

        // Getting appointments from firebase db
        database.getReference("appointments").orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // Getting the appointment values
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Appointment appointment = childSnapshot.getValue(Appointment.class);
                        appointmentList.add(appointment);

                        // Calling the notify() method to notify the user about the appointment
                        notify(appointment);
                    }
                    // Setting the adapter for the recycler view
                    AppointmentAdapter appointmentAdapter = new AppointmentAdapter(AppointmentActivity.this, appointmentList);
                    appointmentRecyclerView.setAdapter(appointmentAdapter);
                    appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(AppointmentActivity.this));
                } else {
                    Toast.makeText(AppointmentActivity.this, "No appointments to show.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }

            // Creating notify() method
            private void notify(Appointment appointment) {
                int notificationId = 1;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(AppointmentActivity.this);
                builder.setSmallIcon(R.mipmap.ic_logo3)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo3_round))
                        .setContentTitle("Medical Appointment")
                        //set the style of your notification and pass parameters for any specific style
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Hello " + username + " you have an appointment - " + appointment.getAppointmentDescription() + " at " + appointment.getAppointmentTime() + " on " + appointment.getAppointmentDate()))
                        .setAutoCancel(true);

                Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(path);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "YOUR_CHANNEL_ID";
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                    builder.setChannelId(channelId);
                }

                notificationManager.notify(notificationId, builder.build());
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