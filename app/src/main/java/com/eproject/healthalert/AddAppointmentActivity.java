package com.eproject.healthalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eproject.healthalert.model.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddAppointmentActivity extends AppCompatActivity {

    // Appointment Id
    private int apptId = 1;

    // Creating form fields
    TextInputEditText desc, loc;
    TimePicker time;
    DatePicker date;
    Button addBtn;
    String userEmail, username, appointmentId, timeVal, dateVal;

    SharedPreferences pref;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Getting the userEmail from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        userEmail = pref.getString("email", "");

        // Getting username from shared preferences
        username = pref.getString("username", "");

        // Initializing form fields
        desc = findViewById(R.id.appointment_desc_edit_val);
        loc = findViewById(R.id.loc_edit_val);
        time = findViewById(R.id.time_edit_val);
        date = findViewById(R.id.date_edit_val);
        addBtn = findViewById(R.id.add_btn);

        // Setting onTimeChangedListener for TimePicker
        time.setIs24HourView(true);
        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                // Setting the timeVal to the time selected by the user
                timeVal = (hourOfDay < 12) ? hourOfDay + ":" + minute + " AM" : hourOfDay + ":" + minute + " PM";
            }
        });

        // Initializing the datePicker to show the current date
        Calendar calender = Calendar.getInstance();
        date.init(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                dateVal = dayOfMonth + "-" + month + "-" + year;
            }
        });

        // Adding the appointment to the database
        addBtn.setOnClickListener(v -> {

            // Checking if fields are empty
            if (desc.getText().toString().isEmpty() || loc.getText().toString().isEmpty()) {
                Toast.makeText(AddAppointmentActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                if (desc.getText().toString().isEmpty()) {
                    desc.setError("Please fill this field");
                }
                if (loc.getText().toString().isEmpty()) {
                    loc.setError("Please fill this field");
                }
            } else {
                // Creating new appointment object
                Appointment appointment = new Appointment(userEmail, apptId++, desc.getText().toString().trim(), dateVal, timeVal, loc.getText().toString().trim());

                // Creating an appointment reference(appointmentId)
                appointmentId = (userEmail.replace("@", "_").replace(".", "_") + appointment.getAppointmentDescription().replace(" ", "").substring(0, 5).toUpperCase() + 000);

                // Checking if the appointment already exists
                database.getReference("appointments").child(appointmentId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(AddAppointmentActivity.this, "There's an error on our end, Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("firebase", "Safe to create appointment", task.getException());

                            // Adding the appointment to the database
                            database.getReference("appointments").child(appointmentId).setValue(appointment);

                            // Calling the notify() method to notify the user about the appointment
                            notify(appointment);

                            // Creating Toast to show success
                            Toast.makeText(AddAppointmentActivity.this, "Appointment added", Toast.LENGTH_SHORT).show();

                            // Redirecting to the AppointmentActivity
                            Intent intent = new Intent(AddAppointmentActivity.this, AppointmentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    // Creating notify() method
                    private void notify(Appointment appointment) {
                        int notificationId = 1;
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(AddAppointmentActivity.this);
                        builder.setSmallIcon(R.mipmap.ic_logo3)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo3_round))
                                .setContentTitle("Medical Appointment")
                                //set the style of your notification and pass parameters for any specific style
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("Hello " + username + ", you've just added an appointment - " + appointment.getAppointmentDescription() + " for " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentTime()))
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
            }
        });

    }
}