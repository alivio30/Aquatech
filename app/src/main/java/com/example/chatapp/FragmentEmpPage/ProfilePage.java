package com.example.chatapp.FragmentEmpPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chatapp.ActivityEmpPage.CreateUser;
import com.example.chatapp.R;

public class ProfilePage extends Fragment {
    View view;
    Button changePassword, createNewAccount;
    ChangePassFragment changePassFragment = new ChangePassFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile_page, container, false);
        changePassword = view.findViewById(R.id.buttonChangePassword);
        createNewAccount = view.findViewById(R.id.buttonCreateNewAccount);

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
            }
        });

        return view;
    }
}