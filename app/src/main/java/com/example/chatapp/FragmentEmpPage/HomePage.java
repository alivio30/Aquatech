package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HomePage extends Fragment {
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView totalUnread, totalRead, totalConsumption, totalActive, totalInactive, totalHouseholds, totalDisconnected;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        totalUnread = view.findViewById(R.id.textTotalUnReadMeter);
        totalRead = view.findViewById(R.id.textTotalReadMeter);
        totalConsumption = view.findViewById(R.id.textTotalWaterConsumption);
        totalActive = view.findViewById(R.id.textTotalActive);
        totalInactive = view.findViewById(R.id.textTotalInActive);
        totalHouseholds = view.findViewById(R.id.textTotalHousehold);
        totalDisconnected = view.findViewById(R.id.textTotalDisconnected);
        countRead();
        countUnread();
        countTotalHouseholds();
        totalConsumption();
        totalActive();
        totalInactive();
        totalDisconnected();

        return view;
    }

    public void countRead(){
        db.collection("consumers").whereEqualTo("remarks", "read")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                counter = counter + 1;
                            }
                            totalRead.setText(String.valueOf(counter));
                        }
                    }
                });
    }

    public void countUnread(){
        db.collection("consumers").whereEqualTo("remarks", "unread")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                counter = counter + 1;
                            }
                            totalUnread.setText(String.valueOf(counter));
                        }
                    }
                });
    }

    public void countTotalHouseholds(){
        db.collection("consumers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                counter = counter + 1;
                            }
                            totalHouseholds.setText(String.valueOf(counter));
                        }
                    }
                });
    }

    public void totalConsumption(){
        db.collection("billing")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int sum = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                sum = sum + Integer.parseInt(ds.getString("ConsumptionUnit"));
                            }
                            totalConsumption.setText(String.valueOf(sum)+"m3");
                        }
                    }
                });
    }

    public void totalActive(){
        db.collection("consumers").whereEqualTo("status", "Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                counter = counter + 1;
                            }
                            totalActive.setText(String.valueOf(counter));
                        }
                    }
                });
    }

    public void totalInactive(){
        db.collection("consumers").whereEqualTo("status", "Inactive")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                counter = counter + 1;
                            }
                            totalInactive.setText(String.valueOf(counter));
                        }
                    }
                });
    }

    public void totalDisconnected(){
        db.collection("consumers").whereEqualTo("status", "Disconnected")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int counter = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                counter = counter + 1;
                            }
                            totalDisconnected.setText(String.valueOf(counter));
                        }
                    }
                });
    }
}