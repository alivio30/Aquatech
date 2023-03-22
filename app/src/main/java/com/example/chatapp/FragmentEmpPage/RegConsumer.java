package com.example.chatapp.FragmentEmpPage;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.Validations;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class RegConsumer extends Fragment {
    PreferenceManager preferenceManager;
    View view, view_activity_create_user;
    StorageReference storageReference;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
    EditText firstRead, inputName, inputAddress, inputContactNumber, inputEmail, inputSerialNumber;
    EditText inputTankNumber, inputPumpNumber, inputLineNumber, inputMeterStand, inputUsername, inputPassword, inputConfirmPassword;
    Button createButton;
    Uri imageUri;
    ProgressDialog progressDialog;
    ImageView backButton, profile;
    int newUserID, newConsumerID;
    Calendar calendar = Calendar.getInstance();
    int year, month, day;
    String consumerAccountNumber, image;
    Spinner spinnerType;
    String type;
    CheckBox chkEmail, chkSms, chkHouse;
    String email = "0", sms = "0", house = "0";
    TextView textAddImage;
    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Validations validations = new Validations();
    UserDetails userDetails = new UserDetails();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reg_consumer, container, false);
        view_activity_create_user = inflater.inflate(R.layout.activity_create_user, container, false);
        backButton = view.findViewById(R.id.imageBack);
        CreateUserFragment createUser = new CreateUserFragment();
        profile = view.findViewById(R.id.imageProfile);
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
        firstRead = view.findViewById(R.id.inputFirstMeterRead);
        textAddImage = view.findViewById(R.id.textAddImage);

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

        preferenceManager = new PreferenceManager(getContext());
        //for account number of a consumer
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //consumerAccountNumber = Integer.toString(year)+ "" +Integer.toString(month)+ "" +generateAccountNumber();
        //randomly generated
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText().toString().trim().isEmpty() || inputAddress.getText().toString().trim().isEmpty() || inputContactNumber.getText().toString().trim().isEmpty() ||
                        inputEmail.getText().toString().trim().isEmpty() || inputSerialNumber.getText().toString().trim().isEmpty() || inputTankNumber.getText().toString().trim().isEmpty() ||
                        inputPumpNumber.getText().toString().trim().isEmpty() || inputLineNumber.getText().toString().trim().isEmpty() || inputMeterStand.getText().toString().trim().isEmpty() ||
                        inputUsername.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().isEmpty() || inputConfirmPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), validations.invalidEmailFormat(), Toast.LENGTH_SHORT).show();
                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    Toast.makeText(getContext(), validations.passwordNotMatched(), Toast.LENGTH_SHORT).show();
                }else if(profile.getDrawable() == null){
                    Toast.makeText(getContext(), validations.nullImage(), Toast.LENGTH_SHORT).show();
                }else if(!chkEmail.isChecked() && !chkSms.isChecked() && !chkHouse.isChecked()){
                    Toast.makeText(getContext(), validations.selectBillMethod(), Toast.LENGTH_SHORT).show();
                }else if(spinnerType.getSelectedItem().toString().equalsIgnoreCase("Select Type")){
                    Toast.makeText(getContext(), validations.selectConsumerType(), Toast.LENGTH_SHORT).show();
                }else if(!validations.isValidEmail(inputEmail.getText().toString().trim())){
                    Toast.makeText(getContext(), validations.invalidEmailFormat(), Toast.LENGTH_SHORT).show();
                }else{
                    validateUserID();
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

    public void validateUserID(){
        newUserID = userID();
        db.collection("users")
                .whereEqualTo("userId", String.valueOf(newUserID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        validateUserID();
                    }else{
                        validateAccountNumber();
                    }
                });

    }
    public void validateAccountNumber(){
        consumerAccountNumber = Integer.toString(year)+ "" +Integer.toString(month)+ "" +generateAccountNumber();
        db.collection("users")
                .whereEqualTo("userId", String.valueOf(newUserID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        validateAccountNumber();
                    }else{
                        validateEmail();
                    }
                });
    }
    public void validateEmail(){
        db.collection("users")
                .whereEqualTo("email", inputEmail.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        Toast.makeText(getContext(), validations.existingEmail(), Toast.LENGTH_SHORT).show();
                    }else{
                        validateConsumerID();
                    }
                });
    }
    public void validateConsumerID(){
        newConsumerID = consumerID();
        db.collection("consumers")
                .whereEqualTo("consId", String.valueOf(newUserID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        validateConsumerID();
                    }else{
                        validateSerialNumber();
                    }
                });
    }
    public void validateSerialNumber(){
        db.collection("consumers")
                .whereEqualTo("meterSerialNumber", inputSerialNumber.getText().toString())
                .get()
                .addOnCompleteListener(userIdTask -> {
                    if (userIdTask.isSuccessful() && userIdTask.getResult() != null && userIdTask.getResult().getDocuments().size() > 0) {
                        Toast.makeText(getContext(), validations.existingSerialNumber(), Toast.LENGTH_SHORT).show();
                    }else{
                        insertUser();
                    }
                });
    }
    public void insertUser(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Creating user account...");
        progressDialog.show();
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image = uri.toString();
                                if(chkEmail.isChecked()){
                                    email = "1";
                                }
                                if(chkSms.isChecked()){
                                    sms = "1";
                                }
                                if(chkHouse.isChecked()){
                                    house = "1";
                                }
                                Toast.makeText(getContext(), image, Toast.LENGTH_SHORT).show();
                                Map<String, Object> createConsumer = new HashMap<>();
                                createConsumer.put("name", inputName.getText().toString());
                                createConsumer.put("accountNumber", consumerAccountNumber);
                                createConsumer.put("meterSerialNumber", inputSerialNumber.getText().toString());
                                createConsumer.put("tankNumber", inputTankNumber.getText().toString());
                                createConsumer.put("pumpNumber", inputPumpNumber.getText().toString());
                                createConsumer.put("lineNumber", inputLineNumber.getText().toString());
                                createConsumer.put("meterStandNumber", inputMeterStand.getText().toString());
                                createConsumer.put("userId", String.valueOf(newUserID));
                                createConsumer.put("consId", String.valueOf(newConsumerID));
                                createConsumer.put("status", "Active");
                                createConsumer.put("remarks", "Unread");
                                createConsumer.put("image", image);
                                createConsumer.put("consumerType", type);
                                createConsumer.put("notifyEmail", email);
                                createConsumer.put("notifySMS", sms);
                                createConsumer.put("notifyHouse", house);
                                createConsumer.put("firstReading", firstRead.getText().toString());
                                createConsumer.put("companyId", userDetails.getCompanyID());

                                //user hash--
                                Map<String, Object> createUser = new HashMap<>();
                                createUser.put("userId", String.valueOf(newUserID));
                                createUser.put("name", inputName.getText().toString());
                                createUser.put("userName", inputUsername.getText().toString());
                                createUser.put("address", inputAddress.getText().toString());
                                createUser.put("contactNumber", inputContactNumber.getText().toString());
                                createUser.put("userType", "Consumer");
                                createUser.put("email", inputEmail.getText().toString());
                                createUser.put("password", inputPassword.getText().toString());
                                createUser.put("status", "Active");
                                createUser.put("image", image);
                                createUser.put("Date Created", year+"-"+month+"-"+day);
                                createUser.put("availability", "0");
                                createUser.put("companyId", userDetails.getCompanyID());

                                //save data to consumers table--
                                db.collection("consumers").document(inputName.getText().toString()+" - "+userDetails.getCompanyName())
                                        .set(createConsumer)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(getContext(), validations.registerSuccess(), Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(exception -> {
                                            Toast.makeText(getContext(), validations.registerFail(), Toast.LENGTH_SHORT).show();
                                        });

                                //save data to users table--
                                db.collection("users").document(inputName.getText().toString()+" - "+userDetails.getCompanyName())
                                        .set(createUser)
                                        .addOnSuccessListener(documentReference -> {
                                            inputName.requestFocus();
                                            clearFields();
                                        })
                                        .addOnFailureListener(exception -> {

                                        });
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "failed to uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_OK);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profile.setImageURI(imageUri);
            textAddImage.setVisibility(View.GONE);
        }
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
    //generate random account number to consumers
    public int generateAccountNumber(){
        Random accountNumberRandom = new Random();
        return ((1 + accountNumberRandom.nextInt(9)) * 10000 + accountNumberRandom.nextInt(10000));
    }
    public void clearFields(){
        profile.setImageDrawable(null);
        textAddImage.setVisibility(View.VISIBLE);
        inputName.setText(null);
        inputAddress.setText(null);
        inputContactNumber.setText(null);
        inputEmail.setText(null);
        inputSerialNumber.setText(null);
        inputTankNumber.setText(null);
        inputPumpNumber.setText(null);
        inputLineNumber.setText(null);
        inputMeterStand.setText(null);
        firstRead.setText(null);
        inputUsername.setText(null);
        inputPassword.setText(null);
        inputConfirmPassword.setText(null);
        chkEmail.setChecked(false);
        chkSms.setChecked(false);
        chkHouse.setChecked(false);
        spinnerType.setSelection(0);
    }
}