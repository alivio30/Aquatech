package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;

public class ProfileDetailsFragment extends Fragment {
    View view;
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    TextView accountNumber, contactNumber, meterSerialNumber, tankNumber, pumpNumber, lineNumber, meterStandNumber;
    TextView meterSerialNumberBill, name, address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        //consumer detail side
        accountNumber = view.findViewById(R.id.textAccNumber);
        contactNumber = view.findViewById(R.id.textContactNumber);
        meterSerialNumber = view.findViewById(R.id.textMeterNumber);
        tankNumber = view.findViewById(R.id.textTank);
        pumpNumber = view.findViewById(R.id.textPump);
        lineNumber = view.findViewById(R.id.textLineNumber);
        meterStandNumber = view.findViewById(R.id.textMeterStand);
        //billing side
        meterSerialNumberBill = view.findViewById(R.id.textSerialNo);
        name = view.findViewById(R.id.textName);
        address = view.findViewById(R.id.textAddress);

        //display consumer detail side
        accountNumber.setText(consumerProfileDetails.getAccountNumber());
        contactNumber.setText(consumerProfileDetails.getContactNumber());
        meterSerialNumber.setText(consumerProfileDetails.getMeterSerialNumber());
        tankNumber.setText(consumerProfileDetails.getTankNumber());
        pumpNumber.setText(consumerProfileDetails.getPumpNumber());
        lineNumber.setText(consumerProfileDetails.getLineNumber());
        meterStandNumber.setText(consumerProfileDetails.getMeterStandNumber());
        //display billing side
        meterSerialNumberBill.setText(consumerProfileDetails.getMeterSerialNumber());
        name.setText(consumerProfileDetails.getName());
        address.setText(consumerProfileDetails.getAddress());

        return view;
    }
}