package com.eproject.healthalert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity {
    ListView l;
    String[] appointment_description
            = { "Appointments 1 , 22 quarry Rd , Ibaara , Abeokuta",
                "Appointments 2 , Cultural Center , Kuto , Abeokuta",
                "Appointments 3 , Obansanjo Library , Abeokuta"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        l.findViewById(R.id.appointment_list_view);

        ArrayAdapter<String> arr = new ArrayAdapter<String>(
                this , com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                appointment_description
        );
        l.setAdapter(arr);

    }
}