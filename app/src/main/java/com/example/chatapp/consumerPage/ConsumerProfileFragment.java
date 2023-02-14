package com.example.chatapp.consumerPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.ChangePassFragment;
import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.Logout;
import com.example.chatapp.utilities.UserDetails;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsumerProfileFragment extends Fragment {
    View view;
    Button changePasswordButton;
    FirebaseFirestore dbUsers = FirebaseFirestore.getInstance();
    FirebaseFirestore dbConsumers = FirebaseFirestore.getInstance();
    UserDetails userDetails = new UserDetails();
    ImageView logout;
    TextView accountNumber, serialNumber, pumpNumber, tankNumber, lineNumber, meterStandNumber, dateApplied, contactNumber, email;
    TextView billNotification, consumerType;
    ChangePassFragment changePassFragment = new ChangePassFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consumer_profile, container, false);
        accountNumber = view.findViewById(R.id.textAccountNumber);
        serialNumber = view.findViewById(R.id.textSerialNumber);
        pumpNumber = view.findViewById(R.id.textPumpNumber);
        tankNumber = view.findViewById(R.id.textTankNumber);
        lineNumber = view.findViewById(R.id.textLineNumber);
        meterStandNumber = view.findViewById(R.id.textMeterStand);
        dateApplied = view.findViewById(R.id.textDateApplied);
        contactNumber = view.findViewById(R.id.textContactNumber);
        email = view.findViewById(R.id.textEmailAddress);


        displayData();

        changePasswordButton = view.findViewById(R.id.buttonChangePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, changePassFragment);
                transaction.commit();
            }
        });
        logout = view.findViewById(R.id.imageLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout logout = new Logout();
                logout.clearAllData();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void displayData(){
        dbUsers.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        dateApplied.setText(documentUserSnapshot.getString("Date Created"));
                        contactNumber.setText(documentUserSnapshot.getString("contactNumber"));
                        email.setText(documentUserSnapshot.getString("email"));
                    }
                });
        dbConsumers.collection("consumers")
                .whereEqualTo("consId", userDetails.getConsumerID())
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        accountNumber.setText(documentUserSnapshot.getString("accountNumber"));
                        serialNumber.setText(documentUserSnapshot.getString("meterSerialNumber"));
                        pumpNumber.setText(documentUserSnapshot.getString("pumpNumber"));
                        tankNumber.setText(documentUserSnapshot.getString("tankNumber"));
                        lineNumber.setText(documentUserSnapshot.getString("lineNumber"));
                        meterStandNumber.setText(documentUserSnapshot.getString("meterStandNumber"));
                    }
                });
    }
    public void clearData(){
        UserDetails userDetails = new UserDetails();
        ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
        userDetails.setName("");
        userDetails.setUsername("");
        userDetails.setPassword("");
        userDetails.setEmail("");
        userDetails.setAddress("");
        userDetails.setContactNumber("");
        //userDetails.setImage("");
        userDetails.setUserType("");
        userDetails.setUserID("");
        userDetails.setConsumerID("");
        userDetails.setSerialNumber("");
        userDetails.setTankNumber("");
        userDetails.setPumpNumber("");
        userDetails.setLineNumber("");
        userDetails.setMeterStandNumber("");
        userDetails.setConsumerType("");

        consumerProfileDetails.setName("");
        consumerProfileDetails.setUserID("");
        consumerProfileDetails.setConsID("");
        consumerProfileDetails.setAccountNumber("");
        consumerProfileDetails.setMeterSerialNumber("");
        consumerProfileDetails.setTankNumber("");
        consumerProfileDetails.setPumpNumber("");
        consumerProfileDetails.setLineNumber("");
        consumerProfileDetails.setMeterStandNumber("");
        consumerProfileDetails.setRemarks("");
        consumerProfileDetails.setStatus("");
        consumerProfileDetails.setConsumerType("");
        consumerProfileDetails.setContactNumber("");
        consumerProfileDetails.setAddress("");
        consumerProfileDetails.setEmail("");
    }
}