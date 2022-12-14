package com.eproject.healthalert;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eproject.healthalert.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView signUp;
    // Creating form fields
    TextInputEditText email, password;
    Button login;

    // Creating Loader Intent
    Intent loaderIntent;

    // Creating an instance of firebase db
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://health-alert-52481-default-rtdb.firebaseio.com");

    // Creating shared preferences
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing Loader Intent
        loaderIntent = new Intent(this, Loader.class);

        // Initializing form fields
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_btn);

        // Setting onClickListener for SignUp TextView
        signUp = findViewById(R.id.sign_up_txt);
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        // Logging the user in
        login.setOnClickListener(v -> {

            String emailVal = email.getText().toString();
            String passwordVal = password.getText().toString();

            // Checking if the fields are empty
            if (emailVal.isEmpty() || passwordVal.isEmpty()) {
                // Showing error message if the fields are empty through Toast
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                if (emailVal.isEmpty()) {
                    email.setError("Field cannot be empty!");
                }
            }

            // Validating email regex
            else if (!email.getText().toString().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                Toast.makeText(this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
                email.setError("Invalid Email Address!");
            } else {
                // Showing Loader
                startActivity(loaderIntent);

                // Checking if user exists in the database
                database.getReference("users").orderByChild("email").equalTo(emailVal).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Checking if the data snapshot is null or not and performing specific actions
                        if (snapshot.getValue() != null) {
                            for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                                User user = childSnapShot.getValue(User.class);
                                if (user != null && user.getPassword().equals(passwordVal)) {
                                    // Showing success message through Toast
                                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    // Starting HomeActivity
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    // Passing user details to SharedPreferences
                                    pref = getSharedPreferences("user", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("email", user.getEmail());
                                    editor.putString("username", user.getFirstName());

                                    // Applying editor changes to SharedPreferences
                                    editor.apply();

                                    // Redirecting to HomeActivity
                                    startActivity(intent);
                                    finish();

                                    // Stopping Loader Activity
                                    Loader.activity.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();

                                    // Setting error message for password field
                                    password.setError("Invalid Password!");

                                    // Stopping Loader Activity
                                    Loader.activity.finish();
                                }
                            }
                        } else {
                            // Displaying Toast message to user that the account doesn't exist
                            Toast.makeText(LoginActivity.this, "Account doesn't exist!", Toast.LENGTH_SHORT).show();

                            // Stopping Loader Activity
                            Loader.activity.finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("The read failed: " + error.getCode());
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