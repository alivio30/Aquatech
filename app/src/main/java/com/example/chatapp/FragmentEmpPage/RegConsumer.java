package com.example.chatapp.FragmentEmpPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.activities.homeActivity;
import com.example.chatapp.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegConsumer extends Fragment {
    View view, view_activity_create_user;
    EditText inputName, inputAddress, inputContactNumber, inputEmail, inputSerialNumber;
    EditText inputTankNumber, inputPumpNumber, inputLineNumber, inputMeterStand, inputUsername, inputPassword, inputConfirmPassword;
    Button createButton;
    int newUserID, newConsumerID;
    Calendar calendar = Calendar.getInstance();
    int year, month;
    String consumerAccountNumber;

    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reg_consumer, container, false);
        view_activity_create_user = inflater.inflate(R.layout.activity_create_user, container, false);

        inputName = view.findViewById(R.id.inputName);
        inputAddress = view.findViewById(R.id.inputAddress);
        inputContactNumber = view.findViewById(R.id.inputContactNumber);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputSerialNumber = view.findViewById(R.id.inputSerialNumber);
        inputTankNumber = view.findViewById(R.id.inputTankNumber);
        inputPumpNumber = view.findViewById(R.id.inputPumpNumber);
        inputLineNumber = view.findViewById(R.id.inputLineNumber);
        inputMeterStand = view.findViewById(R.id.inputMeterStand);
        inputUsername = view.findViewById(R.id.inputUsername);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword);
        createButton = view.findViewById(R.id.buttonCreateNewAccount);
        //for account number of a consumer
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        consumerAccountNumber = Integer.toString(year)+ "" +Integer.toString(month)+ "" +generateAccountNumber();
        //randomly generated

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText().toString().trim().isEmpty() || inputAddress.getText().toString().trim().isEmpty() || inputContactNumber.getText().toString().trim().isEmpty() ||
                        inputEmail.getText().toString().trim().isEmpty() || inputSerialNumber.getText().toString().trim().isEmpty() || inputTankNumber.getText().toString().trim().isEmpty() ||
                        inputPumpNumber.getText().toString().trim().isEmpty() || inputLineNumber.getText().toString().trim().isEmpty() || inputMeterStand.getText().toString().trim().isEmpty() ||
                        inputUsername.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().isEmpty() || inputConfirmPassword.getText().toString().trim().isEmpty()){
                    toast = Toast.makeText(getContext(), "Please input necessary field/s", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    newUserID = userID();
                    newConsumerID = consumerID();
                    insertUser();
                }
            }
        });

        return view;
    }
    public void insertUser() {
        //consumer hash--
        Map<String, Object> createConsumer = new HashMap<>();
        createConsumer.put("name", inputName.getText().toString());
        createConsumer.put("accountNumber", consumerAccountNumber);
        createConsumer.put("meterSerialNumber", inputSerialNumber.getText().toString());
        createConsumer.put("tankNumber", inputTankNumber.getText().toString());
        createConsumer.put("pumpNumber", inputPumpNumber.getText().toString());
        createConsumer.put("lineNumber", inputLineNumber.getText().toString());
        createConsumer.put("meterStandNumber", inputMeterStand.getText().toString());
        createConsumer.put("userId", newUserID);
        createConsumer.put("consId", newConsumerID);
        createConsumer.put("status", "active");
        createConsumer.put("remarks", "unread");
        createConsumer.put("consumerType", "undecided");

        //user hash--
        Map<String, Object> createUser = new HashMap<>();
        createUser.put("userId", newUserID);
        createUser.put("Name", inputName.getText().toString());
        createUser.put("username", inputUsername.getText().toString());
        createUser.put("address", inputAddress.getText().toString());
        createUser.put("contactnumber", inputContactNumber.getText().toString());
        //image line--
        createUser.put("userType", "consumer");
        createUser.put("email", inputEmail.getText().toString());
        createUser.put("password", inputPassword.getText().toString());
        //createUser.put("availability", "not inputted");
        createUser.put("status", "offline");

        //save data to consumers table--
        db.collection("consumers")
                .add(createConsumer)
                .addOnSuccessListener(documentReference -> {
                    toast = Toast.makeText(getContext(), "Inserted to consumers", Toast.LENGTH_SHORT);
                    toast.show();
                })
                .addOnFailureListener(exception -> {
                    toast = Toast.makeText(getContext(), "Insert to consumers failed", Toast.LENGTH_SHORT);
                    toast.show();
                });

        //save data to users table--
        db.collection("users")
                .add(createUser)
                .addOnSuccessListener(documentReference -> {
                    toast = Toast.makeText(getContext(), "Inserted to users", Toast.LENGTH_SHORT);
                    toast.show();
                })
                .addOnFailureListener(exception -> {
                    toast = Toast.makeText(getContext(), "Insert to users failed", Toast.LENGTH_SHORT);
                    toast.show();
                });
    }
    //generate random ID to users
    public int userID() {
        Random userRandom = new Random();
        return ((1 + userRandom.nextInt(9)) * 10000 + userRandom.nextInt(10000));
    }
    //generate random ID to consumers
    public int consumerID() {
        //System.currentTimeMillis()
        Random consumerRandom = new Random();
        return ((1 + consumerRandom.nextInt(9)) * 10000 + consumerRandom.nextInt(10000));
    }

    public int generateAccountNumber(){
        Random accountNumberRandom = new Random();
        return ((1 + accountNumberRandom.nextInt(9)) * 10000 + accountNumberRandom.nextInt(10000));
    }
}