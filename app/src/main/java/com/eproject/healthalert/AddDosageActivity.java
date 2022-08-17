package com.eproject.healthalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eproject.healthalert.model.MedicineDosage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddDosageActivity extends AppCompatActivity {

    // dosage Id
    private int doseId = 1;

    // Creating form fields
    TextInputEditText medName, dose, freq, time;
    DatePicker startDate, endDate;
    Button addBtn;
    String userEmail, dosageId, startDateVal, endDateVal;

    SharedPreferences pref;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dosage);

        // Getting the userEmail from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        userEmail = pref.getString("email", "");

        // Initializing form fields
        medName = findViewById(R.id.med_name);
        dose = findViewById(R.id.dose_edit_val);
        freq = findViewById(R.id.freq_edit_val);
        time = findViewById(R.id.time_interval_edit_val);
        startDate = findViewById(R.id.start_date_edit_val);
        endDate = findViewById(R.id.end_date_edit_val);
        addBtn = findViewById(R.id.add_dosage_btn);

        // Initializing date pickers
        Calendar calender = Calendar.getInstance();
        // Start date
        startDate.init(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                startDateVal = dayOfMonth + "-" + month + "-" + year;
            }
        });
        // End date
        endDate.init(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                endDateVal = dayOfMonth + "-" + month + "-" + year;
            }
        });

        // Adding the dosage to the database
        addBtn.setOnClickListener(v -> {

            // Checking if fields are empty
            if (medName.getText().toString().isEmpty() || dose.getText().toString().isEmpty() || freq.getText().toString().isEmpty() || time.getText().toString().isEmpty()) {
                Toast.makeText(AddDosageActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                if (medName.getText().toString().isEmpty()) {
                    medName.setError("Please fill this field");
                }
                if (dose.getText().toString().isEmpty()) {
                    dose.setError("Please fill this field");
                }
                if (freq.getText().toString().isEmpty()) {
                    freq.setError("Please fill this field");
                }
                if (time.getText().toString().isEmpty()) {
                    time.setError("Please fill this field");
                }
            } else {
                // Creating new dosage object
                MedicineDosage dosage = new MedicineDosage(userEmail, doseId++, medName.getText().toString().trim(), dose.getText().toString(), time.getText().toString(), startDateVal, endDateVal, freq.getText().toString());

                // Creating an dosage reference(dosageId)
                dosageId = (userEmail.replace("@", "_").replace(".", "_") + dosage.getMedicineName().replace(" ", "").substring(0, 5).toUpperCase() + "000");

                // Checking if the dosage already exists
                database.getReference("dosage").child(dosageId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(AddDosageActivity.this, "There's an error on our end, Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("firebase", "Safe to create dosage", task.getException());

                            // Adding the dosage to the database
                            database.getReference("dosages").child(dosageId).setValue(dosage);
                            Toast.makeText(AddDosageActivity.this, "Dosage added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddDosageActivity.this, MedicineDosageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}