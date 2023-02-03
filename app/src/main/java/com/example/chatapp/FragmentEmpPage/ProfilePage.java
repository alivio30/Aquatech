package com.example.chatapp.FragmentEmpPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chatapp.ActivityEmpPage.CreateUser;
import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.utilities.UserDetails;

public class ProfilePage extends Fragment {
    View view;
    Button changePassword, createNewAccount;
    ImageView logoutButton;
    ChangePassFragment changePassFragment = new ChangePassFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile_page, container, false);
        logoutButton = view.findViewById(R.id.imageLogout);
        UserDetails userDetails = new UserDetails();
        changePassword = view.findViewById(R.id.buttonChangePassword);
        createNewAccount = view.findViewById(R.id.buttonCreateNewAccount);
        CreateUserFragment createUserFragment = new CreateUserFragment();

        //change password
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, changePassFragment);
                transaction.commit();
            }
        });

        //create user account
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateUser.class);
                startActivity(intent);
                /**FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, createUserFragment);
                transaction.commit();*/
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
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
    }
}