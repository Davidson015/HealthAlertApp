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

    TextView login;

    // Creating User Object
    User user;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Making the Status Transparent
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

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

            // Setting up User
            user = new User(firstName.getText().toString().trim(), lastName.getText().toString().trim(), email.getText().toString().trim(), age.getText().toString(), genderVal, password.getText().toString(), phoneNo.getText().toString().trim());

            // Capitalizing the first letter of the first name and last name
            user.setFirstName(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1));
            user.setLastName(user.getLastName().substring(0, 1).toUpperCase() + user.getLastName().substring(1));

            // creating a user reference(UserId)
            String userId = user.getFirstName() + user.getLastName();

            // Checking if all fields are Empty
            if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || age.getText().toString().isEmpty() || phoneNo.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                if (firstName.getText().toString().isEmpty()) {
                    firstName.setError("Field cannot be empty!");
                } else if (lastName.getText().toString().isEmpty()) {
                    lastName.setError("Field cannot be empty!");
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Field cannot be empty!");
                } else if (age.getText().toString().isEmpty()) {
                    age.setError("Field cannot be empty!");
                } else if (phoneNo.getText().toString().isEmpty()) {
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
                            Log.e("firebase", "User Already Exists", task.getException());

                            // Creating Toast to show error
                            Toast.makeText(SignUpActivity.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("firebase", "User not found", task.getException());

                            // Adding user to database
                            database.getReference("users").child(userId).setValue(user);
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
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
}