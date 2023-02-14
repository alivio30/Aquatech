package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminBillingDetails extends Fragment {
    View view;
    UserDetails userDetails = new UserDetails();
    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinnerYear, spinnerMonth;
    String getYear, getMonth, filter;
    TextView presentReading, previousReading, waterConsumed, billAmount, penalty, reconnectionFee, serialNumber, meterReader, dueDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_billing_details, container, false);
        presentReading = view.findViewById(R.id.textPresentReading);
        previousReading = view.findViewById(R.id.textPreviousReading);
        waterConsumed = view.findViewById(R.id.textWaterConsumed);
        billAmount = view.findViewById(R.id.textBillAmount);
        penalty = view.findViewById(R.id.textPenalty);
        reconnectionFee = view.findViewById(R.id.textReconnectionFee);
        serialNumber = view.findViewById(R.id.textSerialNo);
        meterReader = view.findViewById(R.id.textMeterReader);
        dueDate = view.findViewById(R.id.textDueDate);
        //spinners
        spinnerYear = view.findViewById(R.id.spinnerYear);
        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        //spinner year
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerYear, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getYear = adapterView.getItemAtPosition(i).toString();
                displayBillingDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //spinner month
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerMonth, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getMonth = adapterView.getItemAtPosition(i).toString();
                displayBillingDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //filter = getMonth+"-"+getYear;
        //displayBillingDetails();

        if(userDetails.getUserType().equalsIgnoreCase("admin")){
            String userID = getArguments().getString("userId");
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
                            //serialNumber.setText(userDetails.getSerialNumber());
                        }
                    });
        }
        if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
            db.collection("consumers")
                    .whereEqualTo("userId", userDetails.getUserID())
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
                            //serialNumber.setText(userDetails.getSerialNumber());
                        }
                    });
        }
        return view;
    }
    public void displayBillingDetails(){
        filter = getMonth+"-"+getYear;
        presentReading.setText("");
        previousReading.setText("");
        waterConsumed.setText("");
        billAmount.setText("");
        penalty.setText("");
        reconnectionFee.setText("");
        meterReader.setText("");
        dueDate.setText("");
        serialNumber.setText("");
        Toast.makeText(getContext(), filter, Toast.LENGTH_LONG).show();
        db.collection("billing")
                .whereEqualTo("readingDate", filter)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot1 = task.getResult().getDocuments().get(0);
                            presentReading.setText(documentSnapshot1.getString("presentReading"));
                            previousReading.setText(documentSnapshot1.getString("previousReading"));
                            waterConsumed.setText(documentSnapshot1.getString("ConsumptionUnit"));
                            billAmount.setText(documentSnapshot1.getLong("billAmount").toString());
                            penalty.setText(documentSnapshot1.getLong("penalty").toString());
                            reconnectionFee.setText(documentSnapshot1.getLong("reconnectionFee").toString());
                            meterReader.setText(documentSnapshot1.getString("MeterReader"));
                            dueDate.setText(documentSnapshot1.getString("dueDate"));
                            db.collection("consumers")
                                    .whereEqualTo("consId", documentSnapshot1.getString("consId"))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                                DocumentSnapshot documentSnapshot2 = task.getResult().getDocuments().get(0);
                                                serialNumber.setText(documentSnapshot2.getString("meterSerialNumber"));
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

}