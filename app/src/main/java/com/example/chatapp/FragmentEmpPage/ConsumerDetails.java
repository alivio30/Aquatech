package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.activities.ProfileDetailsActivity;
import com.example.chatapp.adapters.searchUserAdapter;
import com.example.chatapp.consumerPage.UpdateConsumerFragment;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;

public class ConsumerDetails extends Fragment {
    View view;
    UpdateConsumerFragment updateConsumerFragment = new UpdateConsumerFragment();
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    TextView name, accountNumber, serialNumber, pumpNumber, tankNumber, lineNumber, meterStandNumber, dateApplied;
    TextView contactNumber, email, consumerType, notify;
    ImageView back;
    Button update;
    String notifyVia="", notifyEmail="", notifySMS="", notifyHouse="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_consumer_details, container, false);
        AdminBillingDetails adminBillingDetails = new AdminBillingDetails();
        back = view.findViewById(R.id.imageBack);
        accountNumber = view.findViewById(R.id.textAccountNumber);
        serialNumber = view.findViewById(R.id.textSerialNumber);
        pumpNumber = view.findViewById(R.id.textPumpNumber);
        tankNumber = view.findViewById(R.id.textTankNumber);
        lineNumber = view.findViewById(R.id.textLineNumber);
        meterStandNumber = view.findViewById(R.id.textMeterStand);
        contactNumber = view.findViewById(R.id.textContactNumber);
        dateApplied = view.findViewById(R.id.textDateApplied);
        email = view.findViewById(R.id.textEmailAddress);
        update = view.findViewById(R.id.buttonUpdate);
        consumerType = view.findViewById(R.id.textConsumerType);
        notify = view.findViewById(R.id.textBillNotification);
        if(consumerProfileDetails.getNotifyEmail().equals("1")) notifyVia += "Email, ";
        if(consumerProfileDetails.getNotifySMS().equals("1")) notifyVia += "SMS, ";
        if(consumerProfileDetails.getNotifyHouse().equals("1")) notifyVia += "House, ";


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, updateConsumerFragment);
                transaction.commit();
            }
        });

        accountNumber.setText(consumerProfileDetails.getAccountNumber());
        serialNumber.setText(consumerProfileDetails.getMeterSerialNumber());
        pumpNumber.setText(consumerProfileDetails.getPumpNumber());
        tankNumber.setText(consumerProfileDetails.getTankNumber());
        lineNumber.setText(consumerProfileDetails.getLineNumber());
        meterStandNumber.setText(consumerProfileDetails.getMeterStandNumber());
        contactNumber.setText(consumerProfileDetails.getContactNumber());
        dateApplied.setText(consumerProfileDetails.getDateApplied());
        email.setText(consumerProfileDetails.getEmail());
        consumerType.setText(consumerProfileDetails.getConsumerType());
        notify.setText(notifyVia);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });
        return view;
    }
}