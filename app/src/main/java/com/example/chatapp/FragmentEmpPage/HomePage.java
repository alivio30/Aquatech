package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HomePage extends Fragment {
    View view;
    UserDetails userDetails = new UserDetails();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView totalUnread, totalRead, totalConsumption, totalActive, totalInactive, totalHouseholds, totalDisconnected;
    TextView totalLines, totalPumps, totalTanks;
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
        totalLines = view.findViewById(R.id.textTotalLine);
        totalPumps = view.findViewById(R.id.textTotalPumps);
        totalTanks = view.findViewById(R.id.textTotalTanks);

        displayData(); //method called
        return view;
    }
    //method for displaying all data
    public void displayData(){
        countRead();
        countUnread();
        countTotalHouseholds();
        totalConsumption();
        totalActive();
        totalInactive();
        totalDisconnected();
        totalLines();
        totalPumps();
        totalTanks();
    }
    //method for retrieving the number of "Read" status from all consumers
    public void countRead(){
        db.collection("consumers").whereEqualTo("companyId", userDetails.getCompanyID()).whereEqualTo("remarks", "Read")
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
    //method for retrieving the number of "Unread" status from all consumers
    public void countUnread(){
        db.collection("consumers").whereEqualTo("companyId", userDetails.getCompanyID()).whereEqualTo("remarks", "Unread")
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
    //method for retrieving the number of total households
    public void countTotalHouseholds(){
        db.collection("consumers").whereEqualTo("companyId", userDetails.getCompanyID())
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
    //method for retrieving the total consumption of the company
    public void totalConsumption(){
        db.collection("billing").whereEqualTo("companyId", userDetails.getCompanyID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int sum = 0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                sum = sum + Integer.parseInt(ds.getString("ConsumptionUnit"));
                            }
                            totalConsumption.setText(String.valueOf(sum));
                        }
                    }
                });
    }
    //method for retrieving the number of Active consumers
    public void totalActive(){
        db.collection("consumers").whereEqualTo("companyId", userDetails.getCompanyID()).whereEqualTo("status", "Active")
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
    //method for retrieving the number of Inactive consumers
    public void totalInactive(){
        db.collection("consumers").whereEqualTo("companyId", userDetails.getCompanyID()).whereEqualTo("status", "Inactive")
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
    //method for retrieving the number of Disconnected consumers
    public void totalDisconnected(){
        db.collection("consumers").whereEqualTo("companyId", userDetails.getCompanyID()).whereEqualTo("status", "Disconnected")
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
    //method for retrieving the total Lines of the company
    public void totalLines(){
        db.collection("companyDetails").whereEqualTo("companyId", userDetails.getCompanyID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            totalLines.setText(documentUserSnapshot.getString("totalLines"));
                        }
                    }
                });
    }
    //method for retrieving the total Pumps of the company
    public void totalPumps(){
        db.collection("companyDetails").whereEqualTo("companyId", userDetails.getCompanyID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            totalPumps.setText(documentUserSnapshot.getString("totalPumps"));
                        }
                    }
                });
    }
    //method for retrieving the total Tanks of the company
    public void totalTanks(){
        db.collection("companyDetails").whereEqualTo("companyId", userDetails.getCompanyID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            totalTanks.setText(documentUserSnapshot.getString("totalTanks"));
                        }
                    }
                });
    }
}