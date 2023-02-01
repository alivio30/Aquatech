package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chatapp.R;

public class CreateUserFragment extends Fragment {
    View view;
    Button createConsumer, createMeterReader, createAdmin;
    RegConsumer consumer = new RegConsumer();
    RegMeterReader meterReader = new RegMeterReader();
    RegAdmin admin = new RegAdmin();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_create_user, container, false);

        TextView userAccounts = view.findViewById(R.id.textviewCreateUserAccount);
        createConsumer = view.findViewById(R.id.buttonConsumer);
        createMeterReader = view.findViewById(R.id.buttonMeterReader);
        createAdmin = view.findViewById(R.id.buttonAdmin);

        createConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                //FragmentManager fragmentManager =
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, consumer);
                transaction.commit();
            }
        });
        createMeterReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                //FragmentManager fragmentManager =
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, meterReader);
                transaction.commit();
            }
        });
        createAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                //FragmentManager fragmentManager =
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, admin);
                transaction.commit();
            }
        });


        return view;
    }
}