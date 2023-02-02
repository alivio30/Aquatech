package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegMeterReader extends Fragment {
    View view, view_activity_create_user;
    EditText inputName, inputAddress, inputContactNumber, inputEmail;
    EditText inputUsername, inputPassword, inputConfirmPassword;
    Button createButton;
    ImageView backButton;
    int newUserID;

    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reg_meter_reader, container, false);
        view_activity_create_user = inflater.inflate(R.layout.activity_create_user, container, false);
        backButton = view.findViewById(R.id.imageBack);
        CreateUserFragment createUser = new CreateUserFragment();

        inputName = view.findViewById(R.id.inputName);
        inputAddress = view.findViewById(R.id.inputAddress);
        inputContactNumber = view.findViewById(R.id.inputContactNumber);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputUsername = view.findViewById(R.id.inputUsername);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword);
        createButton = view.findViewById(R.id.buttonCreateNewAccount);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText().toString().trim().isEmpty() || inputAddress.getText().toString().trim().isEmpty() || inputContactNumber.getText().toString().trim().isEmpty() ||
                        inputEmail.getText().toString().trim().isEmpty() || inputUsername.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().isEmpty() ||
                        inputConfirmPassword.getText().toString().trim().isEmpty()){
                    toast = Toast.makeText(getContext(), "Please input necessary field/s", Toast.LENGTH_SHORT);
                    toast.show();

                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    toast = Toast.makeText(getContext(), "Password is not matched!", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    newUserID = userID();
                    insertUser();
                    clearFields();
                    inputName.requestFocus();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, createUser);
                transaction.commit();
            }
        });
        return view;
    }
    public void insertUser() {
        Map<String, Object> createUser = new HashMap<>();
        createUser.put("name", inputName.getText().toString());
        createUser.put("userId", String.valueOf(newUserID));
        createUser.put("userName", inputUsername.getText().toString());
        createUser.put("address", inputAddress.getText().toString());
        createUser.put("contactNumber", inputContactNumber.getText().toString());
        //image line
        createUser.put("userType", "Meter Reader");
        createUser.put("email", inputEmail.getText().toString());
        createUser.put("password", inputPassword.getText().toString());
        //createUser.put("availabilitiy", "not inputted");
        createUser.put("status", "Active");

        //add to users table
        db.collection("users")
                .add(createUser)
                .addOnSuccessListener(documentReference -> {
                    toast = Toast.makeText(getContext(), "Registered Successfully!", Toast.LENGTH_SHORT);
                    toast.show();
                })
                .addOnFailureListener(exception -> {
                    toast = Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_SHORT);
                    toast.show();
                });


    }
    public int userID() {
        Random userRandom = new Random();
        return ((1 + userRandom.nextInt(9)) * 10000 + userRandom.nextInt(10000));
    }
    public void clearFields(){
        inputName.setText(null);
        inputAddress.setText(null);
        inputContactNumber.setText(null);
        inputEmail.setText(null);
        inputUsername.setText(null);
        inputPassword.setText(null);
        inputConfirmPassword.setText(null);
    }
}