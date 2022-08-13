package com.eproject.healthalert.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eproject.healthalert.R;
import com.eproject.healthalert.model.Appointment;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.Viewholder> {

    private Context context;
    private ArrayList<Appointment> appointmentArrayList;

//    Constructor
    public AppointmentAdapter(Context context , ArrayList<Appointment> appointmentArrayList){
        this.context = context;
        this.appointmentArrayList = appointmentArrayList;
    }

    @NonNull
    @Override
    public AppointmentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        to inflate layout for each recycler view item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appoinment_list_card_view,
                parent,
                false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView appointmentDescription , appointmentDate , appointmentTime , appointmentLocation;
        private Boolean appointmentStatus;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            appointmentDescription = itemView.findViewById(R.id.appointment_txt);
            appointmentLocation = itemView.findViewById(R.id.loc_val);
            appointmentDate = itemView.findViewById(R.id.date_val);
            appointmentTime = itemView.findViewById(R.id.time_val);

        }
    }
}
