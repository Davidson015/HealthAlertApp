package com.eproject.healthalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eproject.healthalert.model.PersonalHealthVitals;
import com.eproject.healthalert.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateHealthVitalsActivity extends AppCompatActivity {
    // Creating form fields
    TextInputEditText heartRate, bp, bodyTemp, bloodSugar, height, weight, respRate;
    Button updateBtn;

    String userEmail, healthVitalsId;

    // Vitals value from db
    PersonalHealthVitals dbVitals;

    SharedPreferences pref;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_health_vitals);

        // Getting the userEmail from the SharedPreferences
        pref = getSharedPreferences("user", MODE_PRIVATE);
        userEmail = pref.getString("email", "");

        // Initializing form fields
        heartRate = findViewById(R.id.heart_rate_edit_val);
        bp = findViewById(R.id.bp_edit_val);
        bodyTemp = findViewById(R.id.body_temp_edit_val);
        bloodSugar = findViewById(R.id.blood_sugar_edit_val);
        respRate = findViewById(R.id.respiratory_rate_edit_val);
        height = findViewById(R.id.height_edit_val);
        weight = findViewById(R.id.weight_edit_val);
        updateBtn = findViewById(R.id.update_btn);

        // Getting vitals from the database
        database.getReference("healthVitals").orderByChild("userId").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if the data snapshot is null or not and performing specific actions
                if (snapshot.getValue() != null) {
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                        dbVitals = childSnapShot.getValue(PersonalHealthVitals.class);

                        // Setting fields to default values
                        heartRate.setText(dbVitals.getPulseRate());
                        bp.setText(dbVitals.getBloodPressure());
                        bodyTemp.setText(dbVitals.getBodyTemperature());
                        bloodSugar.setText(dbVitals.getBloodGlucose());
                        respRate.setText(dbVitals.getRespiratoryRate());
                        height.setText(dbVitals.getHeight());
                        weight.setText(dbVitals.getWeight());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        // Adding onClickListener to the update button
        updateBtn.setOnClickListener(v -> {
            // Checking if fields are empty
            if (heartRate.getText().toString().isEmpty() || bp.getText().toString().isEmpty() || bodyTemp.getText().toString().isEmpty() || bloodSugar.getText().toString().isEmpty()|| height.getText().toString().isEmpty() || respRate.getText().toString().isEmpty() || weight.getText().toString().isEmpty())  {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                if (heartRate.getText().toString().isEmpty()) {
                    heartRate.setError("Please fill this field");
                }
                if (bp.getText().toString().isEmpty()) {
                    bp.setError("Please fill this field");
                }
                if (bodyTemp.getText().toString().isEmpty()) {
                    bodyTemp.setError("Please fill this field");
                }
                if (bloodSugar.getText().toString().isEmpty()) {
                    bloodSugar.setError("Please fill this field");
                }
                if (height.getText().toString().isEmpty()) {
                    height.setError("Please fill this field");
                }
                if (weight.getText().toString().isEmpty()) {
                    weight.setError("Please fill this field");
                }
                if (respRate.getText().toString().isEmpty()) {
                    respRate.setError("Please fill this field");
                }

            } else {
                // Creating an instance of PersonalHealthVitals class
                PersonalHealthVitals healthVitals = new PersonalHealthVitals(userEmail, height.getText().toString().trim(), weight.getText().toString().trim(), bp.getText().toString().trim(), bloodSugar.getText().toString().trim(), bodyTemp.getText().toString().trim(), respRate.getText().toString().trim(), heartRate.getText().toString().trim());

                // Creating an health vitals reference(healthVitalsId)
                healthVitalsId = (userEmail.replace("@", "_").replace(".", "_") + "_healthVitals000");

                // Creating/Updating the health vitals in the database
                database.getReference("healthVitals").child(healthVitalsId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(UpdateHealthVitalsActivity.this, "There's an error on our end, Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("firebase", "Safe to create appointment", task.getException());

                            // Updating healthVitals in the database
                            database.getReference("healthVitals").child(healthVitalsId).setValue(healthVitals);
                            Toast.makeText(UpdateHealthVitalsActivity.this, "Health Vitals Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateHealthVitalsActivity.this, HealthActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}