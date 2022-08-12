package com.eproject.healthalert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    ListView l;
    String[] appointment_description
            = { "Appointments 1 , 22 quarry Rd , Ibaara , Abeokuta",
                "Appointments 2 , Cultural Center , Kuto , Abeokuta",
                "Appointments 3 , Obansanjo Library , Abeokuta"};

    String usernameVal;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hiding the Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        usernameVal = getIntent().getStringExtra("username");
        username = findViewById(R.id.username);
        username.setText(String.format("Hello %s", usernameVal));

        l = findViewById(R.id.appointment_list_view);

        ArrayAdapter<String> arr = new ArrayAdapter<String>(
                this , com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                appointment_description
        );
        l.setAdapter(arr);

    }
}