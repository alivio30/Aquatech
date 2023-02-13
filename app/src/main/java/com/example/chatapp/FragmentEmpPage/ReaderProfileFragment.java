package com.example.chatapp.FragmentEmpPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ReaderProfileFragment extends Fragment {
    View view;
    Button buttonChangePassword;
    ImageView logout;
    TextView userId, userName, address, contactNumber, email;
    ChangePassFragment changePassFragment = new ChangePassFragment();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserDetails userDetails = new UserDetails();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reader_profile, container, false);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        userId = view.findViewById(R.id.textEmployeeId);
        userName = view.findViewById(R.id.inputUsername);
        address = view.findViewById(R.id.inputAddress);
        logout = view.findViewById(R.id.imageLogout);

        displayData();

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, changePassFragment);
                transaction.commit();
            }
        });
        /**logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });*/
        return view;
    }
    public void displayData(){
        db.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        userId.setText(documentUserSnapshot.getString("userId"));
                        userName.setText(documentUserSnapshot.getString("userName"));
                        address.setText(documentUserSnapshot.getString("address"));
                        contactNumber.setText(documentUserSnapshot.getString("contactNumber"));
                        email.setText(documentUserSnapshot.getString("email"));
                    }
                });
    }
    public void clearData(){
        UserDetails userDetails = new UserDetails();
        ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
        userDetails.setName("");
        userDetails.setUsername("");
        userDetails.setPassword("");
        userDetails.setEmail("");
        userDetails.setAddress("");
        userDetails.setContactNumber("");
        //userDetails.setImage("");
        userDetails.setUserType("");
        userDetails.setUserID("");
        userDetails.setConsumerID("");
        userDetails.setSerialNumber("");
        userDetails.setTankNumber("");
        userDetails.setPumpNumber("");
        userDetails.setLineNumber("");
        userDetails.setMeterStandNumber("");
        userDetails.setConsumerType("");

        consumerProfileDetails.setName("");
        consumerProfileDetails.setUserID("");
        consumerProfileDetails.setConsID("");
        consumerProfileDetails.setAccountNumber("");
        consumerProfileDetails.setMeterSerialNumber("");
        consumerProfileDetails.setTankNumber("");
        consumerProfileDetails.setPumpNumber("");
        consumerProfileDetails.setLineNumber("");
        consumerProfileDetails.setMeterStandNumber("");
        consumerProfileDetails.setRemarks("");
        consumerProfileDetails.setStatus("");
        consumerProfileDetails.setConsumerType("");
        consumerProfileDetails.setContactNumber("");
        consumerProfileDetails.setAddress("");
        consumerProfileDetails.setEmail("");
    }
}