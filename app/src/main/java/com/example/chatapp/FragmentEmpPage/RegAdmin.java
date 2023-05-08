package com.example.chatapp.FragmentEmpPage;



import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.consumerPage.CMasterPage;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.example.chatapp.databinding.FragmentRegAdminBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.Image;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.Validations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class RegAdmin extends Fragment {
    private FragmentRegAdminBinding binding;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
    Uri imageUri;
    String image;
    TextView textAddImage;
    View view;
    EditText inputName, inputAddress, inputContactNumber, inputEmail;
    EditText inputUsername, inputPassword, inputConfirmPassword;
    Button createButton;
    ImageView backButton, profile;
    int newUserID;
    Calendar calendar = Calendar.getInstance();
    int year, month, day;
    Toast toast;
    UserDetails userDetails = new UserDetails();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Validations validations = new Validations();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reg_admin, container, false);
        //binding = FragmentRegAdminBinding.inflate(getLayoutInflater());
        profile = view.findViewById(R.id.imageProfile);
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
                inputEmail.getText().toString().trim().isEmpty() || inputUsername.getText().toString().trim().isEmpty() ||
                inputPassword.getText().toString().trim().isEmpty() || inputConfirmPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), validations.emptyFields(), Toast.LENGTH_SHORT).show();
                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    Toast.makeText(getContext(), validations.passwordNotMatched(), Toast.LENGTH_SHORT).show();
                }else if(profile.getDrawable() == null){
                    Toast.makeText(getContext(), validations.nullImage(), Toast.LENGTH_SHORT).show();
                }else if(!validations.isValidEmail(inputEmail.getText().toString().trim())){
                    Toast.makeText(getContext(), validations.invalidEmailFormat(), Toast.LENGTH_SHORT).show();
                }else if(inputContactNumber.length() != 11){
                    Toast.makeText(getContext(), validations.contactNumberLimit(), Toast.LENGTH_SHORT).show();
                }
                else{
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
    //validate if user ID is existing
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
    //method for admin register
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
                                Map<String, Object> createUser = new HashMap<>();
                                createUser.put("name", inputName.getText().toString());
                                createUser.put("userId", String.valueOf(newUserID));
                                createUser.put("userName", inputUsername.getText().toString());
                                createUser.put("address", inputAddress.getText().toString());
                                createUser.put("contactNumber", inputContactNumber.getText().toString());
                                createUser.put("userType", "Admin");
                                createUser.put("email", inputEmail.getText().toString());
                                createUser.put("password", inputPassword.getText().toString());
                                createUser.put("status", "Active");
                                createUser.put("image", image);
                                createUser.put("Date Created", year+"-"+month+"-"+day);
                                createUser.put("availability", 0);
                                createUser.put("companyId", userDetails.getCompanyID());

                                //add to users table
                                db.collection("users").document(inputName.getText().toString()+" - "+userDetails.getCompanyName())
                                        .set(createUser)
                                        .addOnSuccessListener(documentReference -> {
                                            toast = Toast.makeText(getContext(), "Registered Successfully!", Toast.LENGTH_SHORT);
                                            toast.show();
                                            clearFields();
                                            inputName.requestFocus();
                                        })
                                        .addOnFailureListener(exception -> {
                                            toast = Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_SHORT);
                                            toast.show();
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
    //method for selecting image
    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_OK);
    }
    //display image after selecting
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profile.setImageURI(imageUri);
            textAddImage.setVisibility(View.GONE);
        }
    }
    //generate random user ID
    public int userID() {
        Random userRandom = new Random();
        return ((1 + userRandom.nextInt(9)) * 10000 + userRandom.nextInt(10000));
    }
    //clear fields after register
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