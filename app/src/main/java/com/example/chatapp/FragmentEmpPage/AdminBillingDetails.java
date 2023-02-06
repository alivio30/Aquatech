package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminBillingDetails extends Fragment {
    View view;
    UserDetails userDetails = new UserDetails();
    TextView serialNumber;
    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_billing_details, container, false);
        String userID = getArguments().getString("userId");
        serialNumber = view.findViewById(R.id.textSerialNo);

        if(userDetails.getUserType().equalsIgnoreCase("admin")){
            toast = Toast.makeText(getContext(), "id: "+userID, Toast.LENGTH_SHORT);
            toast.show();
            db.collection("consumers")
                    .whereEqualTo("userId", userID)
                    .get()
                    .addOnCompleteListener(consumerTask -> {
                        if (consumerTask.isSuccessful() && consumerTask.getResult() != null && consumerTask.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentConsumerSnapshot = consumerTask.getResult().getDocuments().get(0);
                            userDetails.setConsumerID(documentConsumerSnapshot.getString("consId"));
                            userDetails.setSerialNumber(documentConsumerSnapshot.getString("meterSerialNumber"));
                            userDetails.setTankNumber(documentConsumerSnapshot.getString("tankNumber"));
                            userDetails.setPumpNumber(documentConsumerSnapshot.getString("pumpNumber"));
                            userDetails.setLineNumber(documentConsumerSnapshot.getString("lineNumber"));
                            userDetails.setMeterStandNumber(documentConsumerSnapshot.getString("meterStandNumber"));
                            userDetails.setConsumerType(documentConsumerSnapshot.getString("consumerType"));
                            toast = Toast.makeText(getContext(), "address: "+userDetails.getSerialNumber(), Toast.LENGTH_SHORT);
                            toast.show();
                            serialNumber.setText(userDetails.getSerialNumber());
                        }
                    });
        }
        if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
            db.collection("consumers")
                    .whereEqualTo("userId", userDetails.getUserID().toString())
                    .get()
                    .addOnCompleteListener(consumerTask -> {
                        if (consumerTask.isSuccessful() && consumerTask.getResult() != null && consumerTask.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentConsumerSnapshot = consumerTask.getResult().getDocuments().get(0);
                            userDetails.setConsumerID(documentConsumerSnapshot.getString("consId"));
                            userDetails.setSerialNumber(documentConsumerSnapshot.getString("meterSerialNumber"));
                            userDetails.setTankNumber(documentConsumerSnapshot.getString("tankNumber"));
                            userDetails.setPumpNumber(documentConsumerSnapshot.getString("pumpNumber"));
                            userDetails.setLineNumber(documentConsumerSnapshot.getString("lineNumber"));
                            userDetails.setMeterStandNumber(documentConsumerSnapshot.getString("meterStandNumber"));
                            userDetails.setConsumerType(documentConsumerSnapshot.getString("consumerType"));
                            toast = Toast.makeText(getContext(), "address: "+userDetails.getSerialNumber(), Toast.LENGTH_SHORT);
                            toast.show();
                            serialNumber.setText(userDetails.getSerialNumber());
                        }
                    });
        }
        return view;
    }
}