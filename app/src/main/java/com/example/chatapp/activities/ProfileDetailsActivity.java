package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.FragmentEmpPage.ConsumerDetails;
import com.example.chatapp.FragmentEmpPage.ProfileDetailsFragment;
import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileDetailsActivity extends AppCompatActivity {
    ProfileDetailsFragment profileDetailsFragment = new ProfileDetailsFragment();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    ConsumerDetails consumerDetails = new ConsumerDetails();
    String serialNumber;
    Toast toast;
    TextView name, address;
    ImageView consumerProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        serialNumber = getIntent().getStringExtra("serialNumber");
        consumerProfile = findViewById(R.id.imageProfile);
        name = findViewById(R.id.textProfileName);
        address = findViewById(R.id.textAddress);
        //call retrieveConsumerDetails method
        retrieveConsumerDetails(serialNumber);
        address.setText(consumerProfileDetails.getAddress());
        name.setText(consumerProfileDetails.getName());

        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, profileDetailsFragment).commit();

        consumerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerDetails).commit();
            }
        });
    }

    public void retrieveConsumerDetails(String serialNumber){
        db.collection("consumers")
                .whereEqualTo("meterSerialNumber", serialNumber)
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
                        toast = Toast.makeText(getApplicationContext(), consumerProfileDetails.getAccountNumber(), Toast.LENGTH_SHORT);
                        toast.show();

                        db.collection("users")
                                .whereEqualTo("userId", consumerProfileDetails.getUserID())
                                .get()
                                .addOnCompleteListener(userTask1 -> {
                                    if (userTask1.isSuccessful() && userTask1.getResult() != null && userTask1.getResult().getDocuments().size() > 0) {
                                        DocumentSnapshot documentUserSnapshot1 = userTask1.getResult().getDocuments().get(0);
                                        consumerProfileDetails.setContactNumber(documentUserSnapshot1.getString("contactNumber"));
                                        consumerProfileDetails.setAddress(documentUserSnapshot1.getString("address"));
                                        consumerProfileDetails.setEmail(documentUserSnapshot1.getString("email"));
                                        //testing if consumer detail is fetched
                                        toast = Toast.makeText(getApplicationContext(), consumerProfileDetails.getAddress(), Toast.LENGTH_SHORT);
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