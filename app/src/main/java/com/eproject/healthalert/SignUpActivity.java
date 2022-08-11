package com.eproject.healthalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.eproject.healthalert.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    // Creating Form fields
    TextInputEditText firstName, lastName, email, age, phoneNo, password, confirmPassword;
    RadioGroup gender;
    Button signUp;
    String genderVal;

    // Creating User Object
    User user;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initializing Registration Form Fields
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        phoneNo = findViewById(R.id.phone_no);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.con_password);
        signUp = findViewById(R.id.sign_up_btn);

        // Initializing gender RadioGroup
        gender = findViewById(R.id.gender);
        // Setting gender
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.male:
                        gender.check(R.id.male);
                        genderVal = "Male";
                        break;
                    case R.id.female:
                        gender.check(R.id.female);
                        genderVal = "Female";
                        break;
                }
            }
        });

        // Signing up the user
        signUp.setOnClickListener(v -> {

            String userId = firstName.getText().toString() + lastName.getText().toString();

            // Setting up User
            user = new User(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), age.getText().toString(), genderVal, phoneNo.getText().toString(), password.getText().toString());

            // Checking if all fields are Empty
            if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || age.getText().toString().isEmpty() || phoneNo.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                firstName.setError("Please fill this field");
                lastName.setError("Please fill this field");
                email.setError("Please fill this field");
                age.setError("Please fill this field");
                phoneNo.setError("Please fill this field");
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

            // Checking if passwords match
            else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                // Creating Toast to show error
                Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            } else {
                // Checking if the user is already registered
                database.getReference("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("firebase", "User not found", task.getException());

                            // Adding user to database
                            database.getReference("users").child(userId).setValue(user);
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("firebase", "User Already Exists", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(SignUpActivity.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}