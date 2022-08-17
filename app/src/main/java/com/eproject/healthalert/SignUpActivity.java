package com.eproject.healthalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.eproject.healthalert.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    // Creating Form fields
    TextInputEditText firstName, lastName, email, age, phoneNo, password, confirmPassword;
    RadioGroup gender;
    Button signUp;
    String genderVal;

    TextView login;

    // Creating User Object
    User user;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initializing Registration Form Fields
        login = findViewById(R.id.log_in_txt);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        phoneNo = findViewById(R.id.phone_no);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.con_password);
        signUp = findViewById(R.id.sign_up_btn);

        // Setting onClickListener for Login TextView
        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

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

            // Checking if all fields are Empty
            if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || age.getText().toString().isEmpty() || phoneNo.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
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
                if (phoneNo.getText().toString().isEmpty()) {
                    phoneNo.setError("Field cannot be empty!");
                }
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
                user = new User(firstName.getText().toString().trim(), lastName.getText().toString().trim(), email.getText().toString().trim(), age.getText().toString(), genderVal, password.getText().toString(), phoneNo.getText().toString().trim());

                // creating a user reference(UserId)
                String userId = user.getEmail().replace("@", "_").replace(".", "_") + "-000";

                // Checking if the user is already registered
                database.getReference("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(SignUpActivity.this, "There's an error on our end, Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Checking if the user is already registered
                            if (task.getResult().getValue() != null && task.getResult().child("email").getValue().equals(user.getEmail())) {
                                // Creating Toast to show that the email is already registered to another account
                                Toast.makeText(SignUpActivity.this, "Email already registered to another account!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("firebase", "Safe to create user", task.getException());

                                // Adding user to database
                                database.getReference("users").child(userId).setValue(user);

                                // Creating Toast to show that the user is registered successfully
                                Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                                // Ending this activity and starting LoginActivity
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            // Sending Welcome Email to the user
//                            sendWelcomeEmail(user);
                        }
                    }
                });
            }
        });

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

    // Creating the sendWelcomeEmail method
    public void sendWelcomeEmail(User user){
        BackgroundMail.newBuilder(this)
                .withUsername("team.healthalert@gmail.com")
                .withPassword("healthalert3")
                .withMailto(user.getEmail())
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Welcome to Health Alert")
                .withBody("Dear" + user.getFirstName() + ",\n\nWelcome to Health Alert!\n\nWe are glad to have you on board.\n\nRegards,\nHealth Alert Team.")
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        // Logging Success
                        Log.d("backgroundMail", "Welcome Email sent to user successfully");
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        // Logging Failure
                        Log.e("backgroundMail", "Welcome Email sent to user failed");
                    }
                })
                .send();
    }
}