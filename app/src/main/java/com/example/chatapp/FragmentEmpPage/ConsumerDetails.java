package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.activities.ProfileDetailsActivity;
import com.example.chatapp.adapters.searchUserAdapter;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;

public class ConsumerDetails extends Fragment {
    View view;
    AdminBillingDetails adminBillingDetails = new AdminBillingDetails();
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    TextView name, accountNumber, serialNumber, pumpNumber, tankNumber, lineNumber, meterStandNumber, dateApplied;
    TextView contactNumber, email;
    ImageView back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_consumer_details, container, false);
        back = view.findViewById(R.id.imageBack);
        accountNumber = view.findViewById(R.id.textAccountNumber);
        serialNumber = view.findViewById(R.id.textSerialNumber);
        pumpNumber = view.findViewById(R.id.textPumpNumber);
        tankNumber = view.findViewById(R.id.textTankNumber);
        lineNumber = view.findViewById(R.id.textLineNumber);
        meterStandNumber = view.findViewById(R.id.textMeterStand);
        contactNumber = view.findViewById(R.id.textContactNumber);
        email = view.findViewById(R.id.textEmailAddress);

        //name.setText(consumerProfileDetails.getName());
        accountNumber.setText(consumerProfileDetails.getAccountNumber());
        serialNumber.setText(consumerProfileDetails.getMeterSerialNumber());
        pumpNumber.setText(consumerProfileDetails.getPumpNumber());
        tankNumber.setText(consumerProfileDetails.getTankNumber());
        lineNumber.setText(consumerProfileDetails.getLineNumber());
        meterStandNumber.setText(consumerProfileDetails.getMeterStandNumber());
        contactNumber.setText(consumerProfileDetails.getContactNumber());
        email.setText(consumerProfileDetails.getEmail());

        //back.setOnClickListener(v -> getActivity().onBackPressed());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, adminBillingDetails);
                transaction.commit();
            }
        });

        return view;
    }
}