package com.example.chatapp.consumerPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.FragmentEmpPage.ChangePassFragment;
import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.Logout;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ConsumerProfileFragment extends Fragment {
    View view;
    Button changePasswordButton;
    FirebaseFirestore dbUsers = FirebaseFirestore.getInstance();
    FirebaseFirestore dbConsumers = FirebaseFirestore.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserDetails userDetails = new UserDetails();
    ImageView logout;
    TextView accountNumber, serialNumber, pumpNumber, tankNumber, lineNumber, meterStandNumber, dateApplied, contactNumber, email;
    TextView firstRead, billNotification, consumerType;
    String notifyVia="";
    ChangePassFragment changePassFragment = new ChangePassFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consumer_profile, container, false);
        accountNumber = view.findViewById(R.id.textAccountNumber);
        serialNumber = view.findViewById(R.id.textSerialNumber);
        pumpNumber = view.findViewById(R.id.textPumpNumber);
        tankNumber = view.findViewById(R.id.textTankNumber);
        lineNumber = view.findViewById(R.id.textLineNumber);
        meterStandNumber = view.findViewById(R.id.textMeterStand);
        dateApplied = view.findViewById(R.id.textDateApplied);
        contactNumber = view.findViewById(R.id.textContactNumber);
        email = view.findViewById(R.id.textEmailAddress);
        consumerType = view.findViewById(R.id.textConsumerType);
        billNotification = view.findViewById(R.id.textBillNotification);
        firstRead = view.findViewById(R.id.textFirstMeterRead);

        displayData();

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
        logout = view.findViewById(R.id.imageLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eraseToken();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void displayData(){
        dbUsers.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        dateApplied.setText(documentUserSnapshot.getString("Date Created"));
                        contactNumber.setText(documentUserSnapshot.getString("contactNumber"));
                        email.setText(documentUserSnapshot.getString("email"));
                    }
                });
        dbConsumers.collection("consumers")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        accountNumber.setText(documentUserSnapshot.getString("accountNumber"));
                        serialNumber.setText(documentUserSnapshot.getString("meterSerialNumber"));
                        pumpNumber.setText(documentUserSnapshot.getString("pumpNumber"));
                        tankNumber.setText(documentUserSnapshot.getString("tankNumber"));
                        lineNumber.setText(documentUserSnapshot.getString("lineNumber"));
                        meterStandNumber.setText(documentUserSnapshot.getString("meterStandNumber"));
                        consumerType.setText(documentUserSnapshot.getString("consumerType"));
                        if(documentUserSnapshot.getString("notifyEmail").equals("1") && documentUserSnapshot.getString("notifyHouse").equals("1") && documentUserSnapshot.getString("notifySMS").equals("1")){
                            notifyVia = "Email, SMS, House";
                        }else if(documentUserSnapshot.getString("notifyEmail").equals("1") && documentUserSnapshot.getString("notifyHouse").equals("1")){
                            notifyVia = "Email, House";
                        }else if(documentUserSnapshot.getString("notifyEmail").equals("1") && documentUserSnapshot.getString("notifySMS").equals("1")){
                            notifyVia = "Email, SMS";
                        }else if(documentUserSnapshot.getString("notifySMS").equals("1") && documentUserSnapshot.getString("notifyHouse").equals("1")){
                            notifyVia = "SMS, House";
                        }else if(documentUserSnapshot.getString("notifyEmail").equals("1")){
                            notifyVia = "Email";
                        }else if(documentUserSnapshot.getString("notifySMS").equals("1")){
                            notifyVia = "SMS";
                        }else if(documentUserSnapshot.getString("notifyHouse").equals("1")){
                            notifyVia = "House";
                        }
                        billNotification.setText(notifyVia);
                        firstRead.setText(documentUserSnapshot.getString("firstReading"));
                    }
                });
    }
    public void eraseToken(){
        Toast.makeText(getContext(), "Signing out...", Toast.LENGTH_SHORT).show();
        db.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            HashMap<String, Object> clearToken = new HashMap<>();
                            clearToken.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                            db.collection("users")
                                    .document(documentID)
                                    .update(clearToken)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Logout logout = new Logout();
                                            logout.clearAllData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Unable to sign out", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }
}