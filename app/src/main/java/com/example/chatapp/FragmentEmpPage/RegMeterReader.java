package com.example.chatapp.FragmentEmpPage;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.Validations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class RegMeterReader extends Fragment {
    View view, view_activity_create_user;
    EditText inputName, inputAddress, inputContactNumber, inputEmail;
    EditText inputUsername, inputPassword, inputConfirmPassword;
    Button createButton;
    TextView textAddImage;
    ImageView backButton, profile;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String image;
    Uri imageUri;
    int newUserID;
    Calendar calendar = Calendar.getInstance();
    int year, month,day;
    UserDetails userDetails = new UserDetails();

    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Validations validations = new Validations();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reg_meter_reader, container, false);
        view_activity_create_user = inflater.inflate(R.layout.activity_create_user, container, false);
        backButton = view.findViewById(R.id.imageBack);
        CreateUserFragment createUser = new CreateUserFragment();
        profile = view.findViewById(R.id.imageProfile);
        inputName = view.findViewById(R.id.inputName);
        inputAddress = view.findViewById(R.id.inputAddress);
        inputContactNumber = view.findViewById(R.id.inputContactNumber);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputUsername = view.findViewById(R.id.inputUsername);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword);
        createButton = view.findViewById(R.id.buttonCreateNewAccount);
        textAddImage = view.findViewById(R.id.textAddImage);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if(inputName.getText().toString().trim().isEmpty() || inputAddress.getText().toString().trim().isEmpty() || inputContactNumber.getText().toString().trim().isEmpty() ||
                        inputEmail.getText().toString().trim().isEmpty() || inputUsername.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().isEmpty() ||
                        inputConfirmPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), validations.emptyFields(), Toast.LENGTH_SHORT).show();
                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    Toast.makeText(getContext(), validations.passwordNotMatched(), Toast.LENGTH_SHORT).show();
                }else if(profile.getDrawable() == null){
                    Toast.makeText(getContext(), validations.nullImage(), Toast.LENGTH_SHORT).show();
                }else if(!validations.isValidEmail(inputEmail.getText().toString().trim())){
                    Toast.makeText(getContext(), validations.invalidEmailFormat(), Toast.LENGTH_SHORT).show();
                }else if(inputContactNumber.length() != 11){
                    Toast.makeText(getContext(), validations.contactNumberLimit(), Toast.LENGTH_SHORT).show();
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
    //validates if user id is already existing
    public void validateUserID(){
        newUserID = userID();
        db.collection("users")
                .whereEqualTo("userId", String.valueOf(newUserID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        validateUserID();
                    }else{
                        insertUser();
                    }
                });

    }
    //method for meter reader register
    public void insertUser(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Creating user account...");
        progressDialog.show();

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
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
                                //user hash--
                                Map<String, Object> createUser = new HashMap<>();
                                createUser.put("userId", String.valueOf(newUserID));
                                createUser.put("name", inputName.getText().toString());
                                createUser.put("userName", inputUsername.getText().toString());
                                createUser.put("address", inputAddress.getText().toString());
                                createUser.put("contactNumber", inputContactNumber.getText().toString());
                                createUser.put("userType", "Meter Reader");
                                createUser.put("email", inputEmail.getText().toString());
                                createUser.put("password", inputPassword.getText().toString());
                                //createUser.put("availability", "not inputted");
                                createUser.put("status", "Active");
                                createUser.put("image", image);
                                createUser.put("Date Created", year+"-"+month+"-"+day);
                                createUser.put("availability", 0);
                                createUser.put("companyId", userDetails.getCompanyID());

                                //save data to users table--
                                db.collection("users").document(inputName.getText().toString()+" - "+userDetails.getCompanyName())
                                        .set(createUser)
                                        .addOnSuccessListener(documentReference -> {
                                            inputName.requestFocus();
                                            clearFields();
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
    //method for image selecting
    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_OK);
    }
    //displays image after selecting
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profile.setImageURI(imageUri);
            textAddImage.setVisibility(View.GONE);
        }
    }
    //generates random userID
    public int userID() {
        Random userRandom = new Random();
        return ((1 + userRandom.nextInt(9)) * 10000 + userRandom.nextInt(10000));
    }
    //method for clearing fields after register
    public void clearFields(){
        profile.setImageDrawable(null);
        textAddImage.setVisibility(View.VISIBLE);
        inputName.setText(null);
        inputAddress.setText(null);
        inputContactNumber.setText(null);
        inputEmail.setText(null);
        inputUsername.setText(null);
        inputPassword.setText(null);
        inputConfirmPassword.setText(null);
    }
}