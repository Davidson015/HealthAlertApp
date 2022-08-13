package com.eproject.healthalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.eproject.healthalert.adapter.AppointmentAdapter;
import com.eproject.healthalert.model.Appointment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {

    CardView card;
    Button addbtn;
    Intent intent;

    private RecyclerView appointment_recyclerView;

    private ArrayList<Appointment> appointmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

//        Setting Recycler View for Appointment cards
        appointment_recyclerView = findViewById(R.id.recyclerview);

        appointmentArrayList = new ArrayList<>();

        appointmentArrayList.add(new Appointment("","","Dr Adam Smith ,Dentist Meeting","21st Aug 2022","10:00 am","New City Clinic",true));
        appointmentArrayList.add(new Appointment("","","Dr Adam Smith ,Dentist Meeting","21st Aug 2022","10:00 am","New City Clinic",true));
        appointmentArrayList.add(new Appointment("","","Dr Adam Smith ,Dentist Meeting","21st Aug 2022","10:00 am","New City Clinic",true));
        appointmentArrayList.add(new Appointment("",""," Dr Angel Smith,Gynecologist","23rd Sep 2022","08:00 am","Lagoon Hospital",true));
        appointmentArrayList.add(new Appointment("",""," Dr Angel Smith,Gynecologist Checkup","23rd Sep 2022","08:00 am","Lagoon Hospital",false));
        appointmentArrayList.add(new Appointment("","","Dr Beau Hightower , Chiropractic Adjustment ","21st Aug 2022","10:00 am","New City Clinic",true));


        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this,appointmentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        appointment_recyclerView.setLayoutManager(linearLayoutManager);
        appointment_recyclerView.setAdapter(appointmentAdapter);

        // make card checkable

    }
}