package com.example.chatapp.consumerPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chatapp.FragmentEmpPage.ChangePassFragment;
import com.example.chatapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsumerProfileFragment extends Fragment {
    View view;
    Button changePasswordButton;
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
        return view;
    }
}