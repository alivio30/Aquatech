package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.example.chatapp.consumerPage.ConsumerProfileFragment;
import com.example.chatapp.utilities.UserDetails;

public class ChangePassFragment extends Fragment {
    UserDetails userDetails = new UserDetails();
    View view;
    ImageView backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        backButton = view.findViewById(R.id.imageBack);
        ConsumerProfileFragment consumerProfile = new ConsumerProfileFragment();
        ProfilePage adminProfile = new ProfilePage();
        ReaderProfileFragment readerProfile = new ReaderProfileFragment();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                /**transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, profilePage);
                transaction.commit();*/
                if(userDetails.getUserType().equalsIgnoreCase("consumer")){
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.FragmentContainer, consumerProfile);
                    transaction.commit();
                }
                if(userDetails.getUserType().equalsIgnoreCase("meter reader")){
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.FragmentContainer, readerProfile);
                    transaction.commit();
                }
                if(userDetails.getUserType().equalsIgnoreCase("admin")){
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.FragmentContainer, adminProfile);
                    transaction.commit();
                }
            }
        });

        return view;
    }
}