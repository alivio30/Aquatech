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

import com.example.chatapp.FragmentEmpPage.ChangePassFragment;
import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsumerProfileFragment extends Fragment {
    View view;
    Button changePasswordButton;
    ImageView logout;
    ChangePassFragment changePassFragment = new ChangePassFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consumer_profile, container, false);
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
                clearData();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        return view;
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