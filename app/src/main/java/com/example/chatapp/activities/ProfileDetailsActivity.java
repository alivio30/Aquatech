package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.FragmentEmpPage.AdminBillingDetails;
import com.example.chatapp.FragmentEmpPage.ConsumerDetails;
import com.example.chatapp.FragmentEmpPage.ProfileDetailsFragment;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.example.chatapp.adapters.RecyclerViewInterface;
import com.example.chatapp.adapters.searchUserAdapter;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetailsRecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileDetailsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    AdminBillingDetails adminBillingDetails = new AdminBillingDetails();
    ConsumerDetails consumerDetails = new ConsumerDetails();
    String name, userID;
    Toast toast;
    TextView txtname, txtaddress;
    ImageView consumerProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, adminBillingDetails).commit();
        name = getIntent().getStringExtra("name");
        this.userID = getIntent().getStringExtra("userID");

        //initiation of variables
        consumerProfile = findViewById(R.id.imageProfile);
        txtname = findViewById(R.id.textProfileName);
        txtaddress = findViewById(R.id.textAddress);

        //retrieve consumer data
        retrieveConsumerData();

        //display data of a consumer
        txtname.setText(name);
        txtaddress.setText(consumerProfileDetails.getAddress());

        consumerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerDetails).commit();
            }
        });
    }

    public void retrieveConsumerData(){
        db.collection("consumers")
                .whereEqualTo("userId", this.userID)
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful() && userTask.getResult() != null && userTask.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = userTask.getResult().getDocuments().get(0);
                        consumerProfileDetails.setName(documentUserSnapshot.getString("name"));
                        consumerProfileDetails.setUserID(documentUserSnapshot.getString("userId"));
                        consumerProfileDetails.setConsID(documentUserSnapshot.getString("consId"));
                        consumerProfileDetails.setAccountNumber(documentUserSnapshot.getString("accountNumber"));
                        consumerProfileDetails.setMeterSerialNumber(documentUserSnapshot.getString("meterSerialNumber"));
                        consumerProfileDetails.setTankNumber(documentUserSnapshot.getString("tankNumber"));
                        consumerProfileDetails.setPumpNumber(documentUserSnapshot.getString("pumpNumber"));
                        consumerProfileDetails.setLineNumber(documentUserSnapshot.getString("lineNumber"));
                        consumerProfileDetails.setMeterStandNumber(documentUserSnapshot.getString("meterStandNumber"));
                        consumerProfileDetails.setRemarks(documentUserSnapshot.getString("remarks"));
                        consumerProfileDetails.setStatus(documentUserSnapshot.getString("status"));
                        consumerProfileDetails.setConsumerType(documentUserSnapshot.getString("consumerType"));
                        //testing if consumer detail is fetched
                        toast = Toast.makeText(getApplicationContext(), consumerProfileDetails.getUserID(), Toast.LENGTH_SHORT);
                        toast.show();

                        db.collection("users")
                                .whereEqualTo("userId", userID)
                                .get()
                                .addOnCompleteListener(userTask1 -> {
                                    if (userTask1.isSuccessful() && userTask1.getResult() != null && userTask1.getResult().getDocuments().size() > 0) {
                                        DocumentSnapshot documentUserSnapshot1 = userTask1.getResult().getDocuments().get(0);
                                        consumerProfileDetails.setContactNumber(documentUserSnapshot1.getString("contactNumber"));
                                        consumerProfileDetails.setAddress(documentUserSnapshot1.getString("address"));
                                        consumerProfileDetails.setEmail(documentUserSnapshot1.getString("email"));
                                        //testing if consumer detail is fetched
                                        toast = Toast.makeText(getApplicationContext(), consumerProfileDetails.getEmail(), Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        toast = Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "consumer does not exist!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }
}