package com.eproject.healthalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.eproject.healthalert.model.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class AddAppointmentActivity extends AppCompatActivity {

    // Appointment Id
    private int apptId = 1;

    // Creating form fields
    TextInputEditText desc, loc, time, date;
    Button addBtn;
    String userEmail;

    String appointmentId;

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

        // Initializing form fields
        desc = findViewById(R.id.appointment_desc_edit_val);
        loc = findViewById(R.id.loc_edit_val);
        time = findViewById(R.id.time_edit_val);
        date = findViewById(R.id.date_edit_val);
        addBtn = findViewById(R.id.add_btn);

        // Adding the appointment to the database
        addBtn.setOnClickListener(v -> {

            // Checking if fields are empty
            if (desc.getText().toString().isEmpty() || loc.getText().toString().isEmpty() || time.getText().toString().isEmpty() || date.getText().toString().isEmpty()) {
                Toast.makeText(AddAppointmentActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                if (desc.getText().toString().isEmpty()) {
                    desc.setError("Please fill this field");
                } else if (loc.getText().toString().isEmpty()) {
                    loc.setError("Please fill this field");
                } else if (time.getText().toString().isEmpty()) {
                    time.setError("Please fill this field");
                } else if (date.getText().toString().isEmpty()) {
                    date.setError("Please fill this field");
                }
            } else {
                // Creating new appointment object
                Appointment appointment = new Appointment(userEmail, apptId++, desc.getText().toString().trim(), date.getText().toString().trim(), time.getText().toString().trim(), loc.getText().toString().trim());

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
                            Toast.makeText(AddAppointmentActivity.this, "Appointment added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddAppointmentActivity.this, AppointmentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

    }
}