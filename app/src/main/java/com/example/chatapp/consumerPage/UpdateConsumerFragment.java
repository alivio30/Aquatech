package com.example.chatapp.consumerPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UpdateConsumerFragment extends Fragment {
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View view;
    ImageView image;
    EditText inputName, inputAddress, inputContactNumber, inputEmail, inputSerialNumber, inputTankNumber, inputPumpNumber, inputLineNumber, inputMeterStand;
    EditText inputUsername, inputPassword, inputConfirmPassword;
    CheckBox chkEmail, chkSms, chkHouse;
    String email = "0", sms = "0", house = "0";
    Spinner spinnerType;
    Button updateButton;
    String type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update_consumer, container, false);
        image = view.findViewById(R.id.imageProfile);
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
        updateButton = view.findViewById(R.id.buttonUpdateAccount);

        //checkbox preferred notification method
        chkEmail = view.findViewById(R.id.checkBoxEmail);
        chkSms = view.findViewById(R.id.checkBoxSMS);
        chkHouse = view.findViewById(R.id.checkBoxHouse);

        //spinner Consumer Type
        spinnerType = view.findViewById(R.id.spinnerConsumerType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        displayData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if(inputName.getText().toString().trim().isEmpty() || inputAddress.getText().toString().trim().isEmpty() ||
                   inputContactNumber.getText().toString().trim().isEmpty() || inputEmail.getText().toString().trim().isEmpty() ||
                   inputSerialNumber.getText().toString().trim().isEmpty() || inputTankNumber.getText().toString().trim().isEmpty() ||
                   inputTankNumber.getText().toString().trim().isEmpty() || inputPumpNumber.getText().toString().trim().isEmpty() ||
                   inputLineNumber.getText().toString().trim().isEmpty() || inputMeterStand.getText().toString().trim().isEmpty() ||
                   inputUsername.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().isEmpty() ||
                   inputConfirmPassword.getText().toString().trim().isEmpty() || type.equals("Select Type")){
                    Toast.makeText(getContext(), "Please input necessary fields!", Toast.LENGTH_SHORT).show();
                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    Toast.makeText(getContext(), "Password not matched!", Toast.LENGTH_SHORT).show();
                }else if(email == "0" && sms == "0" && house == "0"){
                    Toast.makeText(getContext(), "Select at least 1 preferred notification!", Toast.LENGTH_SHORT).show();
                }else{
                    updateAccount();
                }
            }
        });
        return view;
    }

    public void updateAccount(){
        if(chkEmail.isChecked()){
            email = "1";
        }
        if(chkSms.isChecked()){
            sms = "1";
        }
        if(chkHouse.isChecked()){
            house = "1";
        }
        Map<String, Object> updateConsumers = new HashMap<>();
        updateConsumers.put("name", inputName.getText().toString());
        updateConsumers.put("meterSerialNumber", inputSerialNumber.getText().toString());
        updateConsumers.put("tankNumber", inputTankNumber.getText().toString());
        updateConsumers.put("pumpNumber", inputPumpNumber.getText().toString());
        updateConsumers.put("lineNumber", inputLineNumber.getText().toString());
        updateConsumers.put("meterStandNumber", inputMeterStand.getText().toString());
        updateConsumers.put("notifyEmail", email);
        updateConsumers.put("notifySMS", sms);
        updateConsumers.put("notifyHouse", house);
        updateConsumers.put("consumerType", type);

        Map<String, Object> updateUsers = new HashMap<>();
        updateUsers.put("address", inputAddress.getText().toString());
        updateUsers.put("contactNumber", inputContactNumber.getText().toString());
        updateUsers.put("email", inputEmail.getText().toString());
        updateUsers.put("userName", inputUsername.getText().toString());
        updateUsers.put("password", inputPassword.getText().toString());
        db.collection("consumers")
                .whereEqualTo("consId", consumerProfileDetails.getConsID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentUserSnapshot.getId();
                            db.collection("consumers")
                                    .document(documentID)
                                    .update(updateConsumers);
                        }
                    }
                });
        db.collection("users")
                .whereEqualTo("userId", consumerProfileDetails.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentUserSnapshot.getId();
                            db.collection("users")
                                    .document(documentID)
                                    .update(updateUsers)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }

    public void displayData(){
        String imageUrl = null;
        imageUrl = consumerProfileDetails.getImage();
        Picasso.get().load(imageUrl).into(image);
        inputName.setText(consumerProfileDetails.getName());
        inputAddress.setText(consumerProfileDetails.getAddress());
        inputContactNumber.setText(consumerProfileDetails.getContactNumber());
        inputEmail.setText(consumerProfileDetails.getEmail());
        inputSerialNumber.setText(consumerProfileDetails.getMeterSerialNumber());
        inputTankNumber.setText(consumerProfileDetails.getTankNumber());
        inputPumpNumber.setText(consumerProfileDetails.getPumpNumber());
        inputLineNumber.setText(consumerProfileDetails.getLineNumber());
        inputMeterStand.setText(consumerProfileDetails.getMeterStandNumber());
        inputUsername.setText(consumerProfileDetails.getUserName());
        inputPassword.setText(consumerProfileDetails.getPassword());
    }
}