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
import android.widget.Toast;

import com.example.chatapp.R;
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
    ImageView backButton, profile;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String image;
    Uri imageUri;
    int newUserID;
    Calendar calendar = Calendar.getInstance();
    int year, month,day;

    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                if(inputName.getText().toString().trim().isEmpty() || inputAddress.getText().toString().trim().isEmpty() || inputContactNumber.getText().toString().trim().isEmpty() ||
                        inputEmail.getText().toString().trim().isEmpty() || inputUsername.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().isEmpty() ||
                        inputConfirmPassword.getText().toString().trim().isEmpty()){
                    toast = Toast.makeText(getContext(), "Please input necessary field/s", Toast.LENGTH_SHORT);
                    toast.show();

                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    toast = Toast.makeText(getContext(), "Password is not matched!", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(profile.getDrawable() == null){
                    toast = Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    newUserID = userID();
                    db.collection("users")
                            .whereEqualTo("password", inputPassword.getText().toString())
                            .get()
                            .addOnCompleteListener(passwordTask -> {
                                if (passwordTask.isSuccessful() && passwordTask.getResult() != null && passwordTask.getResult().getDocuments().size() > 0) {
                                    DocumentSnapshot documentUserSnapshot = passwordTask.getResult().getDocuments().get(0);
                                }else{
                                    toast = Toast.makeText(getContext(), "password is new", Toast.LENGTH_SHORT);
                                    toast.show();
                                    db.collection("users")
                                            .whereEqualTo("userId", String.valueOf(newUserID))
                                            .get()
                                            .addOnCompleteListener(userIdTask ->{
                                                if (passwordTask.isSuccessful() && passwordTask.getResult() != null && passwordTask.getResult().getDocuments().size() > 0) {
                                                    DocumentSnapshot documentUserSnapshot = passwordTask.getResult().getDocuments().get(0);
                                                    toast = Toast.makeText(getContext(), "userId already existed", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }else{
                                                    insertUser();
                                                }
                                            });
                                }
                            });
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
    public void insertUser(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Uploading File...");
        progressDialog.show();

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //binding.imageProfile.setImageURI(null);
                        Toast.makeText(getContext(), "successfully uploaded", Toast.LENGTH_SHORT).show();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image = uri.toString();
                                Toast.makeText(getContext(), image, Toast.LENGTH_SHORT).show();
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
                                createUser.put("availability", "0");

                                //save data to users table--
                                db.collection("users")
                                        .add(createUser)
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
        }
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