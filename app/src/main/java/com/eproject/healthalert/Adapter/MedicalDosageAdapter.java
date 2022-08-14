package com.eproject.healthalert.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eproject.healthalert.R;
import com.eproject.healthalert.model.MedicineDosage;

import java.util.ArrayList;


public class MedicalDosageAdapter extends RecyclerView.Adapter<MedicalDosageAdapter.Viewholder> {

    private Context context;
    private ArrayList<MedicineDosage> dosageArrayList;

    //    Constructor
    public MedicalDosageAdapter(Context context, ArrayList<MedicineDosage> dosageArrayList) {
        this.context = context;
        this.dosageArrayList = dosageArrayList;
    }

    @NonNull
    @Override
    public MedicalDosageAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        to inflate layout for each recycler view item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_dosage_list_card_view,
                parent,
                false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalDosageAdapter.Viewholder holder, int position) {
        MedicineDosage model = dosageArrayList.get(position);
        holder.medicineName.setText(model.getMedicineName());
        holder.medicineDosage.setText(model.getMedicineDosage());
//        holder.medicineDosageStartDate.setText(model.getMedicineDosageStartDate());
//        holder.medicineDosageEndDate.setText(model.getMedicineDosageEndDate());
        holder.medicineDosageTime.setText(model.getMedicineDosageTime());
        holder.medicineDosageFrequency.setText(model.getMedicineDosageFrequency());

    }

    @Override
    public int getItemCount() {
        return dosageArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView userId;
        private TextView medicineId;
        private final TextView medicineName;
        private final TextView medicineDosage;
        private final TextView medicineDosageTime;
        //        private TextView medicineDosageStartDate;
        private TextView medicineDosageEndDate;
        private final TextView medicineDosageFrequency;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine);
            medicineDosage = itemView.findViewById(R.id.dosage);
//            medicineDosageStartDate = itemView.findViewById(R.id.interval);
            medicineDosageTime = itemView.findViewById(R.id.interval);

            //medicineDosageEndDate =
            medicineDosageFrequency = itemView.findViewById(R.id.tablet_no);

        }
    }
}

