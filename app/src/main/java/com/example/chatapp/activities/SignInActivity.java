package com.example.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.consumerPage.CMasterPage;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    EditText username, password;
    Button button;
    String name;
    ProgressBar progressBar;
    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        button = findViewById(R.id.buttonSignIn);
        progressBar = findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    signIn();
            }
        });

        /**preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MasterPage.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();*/
    }
    public void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        UserDetails userDetails = new UserDetails();
        db.collection("users")
                .whereEqualTo("userName", username.getText().toString())
                .whereEqualTo("password", password.getText().toString())
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful() && userTask.getResult() != null && userTask.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = userTask.getResult().getDocuments().get(0);
                        userDetails.setName(documentUserSnapshot.getString("name"));
                        userDetails.setUsername(documentUserSnapshot.getString("userName"));
                        userDetails.setPassword(documentUserSnapshot.getString("password"));
                        userDetails.setAddress(documentUserSnapshot.getString("address"));
                        userDetails.setEmail(documentUserSnapshot.getString("email"));
                        userDetails.setContactNumber(documentUserSnapshot.getString("contactNumber"));
                        //userDetails.setImage(task.getResult().getString("image"));
                        userDetails.setUserType(documentUserSnapshot.getString("userType"));
                        userDetails.setUserID(documentUserSnapshot.getString("userId"));
                        userDetails.setImage(documentUserSnapshot.getString("image"));
                        //testing if user detail is fetched
                        toast = Toast.makeText(getApplicationContext(), userDetails.getUserType(), Toast.LENGTH_SHORT);
                        toast.show();

                        if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
                            db1.collection("consumers")
                                    .whereEqualTo("userId", userDetails.getUserID().toString())
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
                                            toast = Toast.makeText(getApplicationContext(), userDetails.getSerialNumber(), Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                        }
                        progressBar.setVisibility(View.GONE);
                        if (userDetails.getUserType().equalsIgnoreCase("admin")) {
                            Intent intent = new Intent(this, MasterPage.class);
                            startActivity(intent);
                        }
                        if (userDetails.getUserType().equalsIgnoreCase("meter reader")) {
                            Intent intent = new Intent(this, MasterPage.class);
                            startActivity(intent);
                        }
                        if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
                            Intent intent = new Intent(this, CMasterPage.class);
                            startActivity(intent);
                        }
                        //clearFields();
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }
    public void clearFields(){
        username.setText(null);
        password.setText(null);
    }
    /**private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(v -> {
            if(isValidSignInDetails()) {
               signIn();
            }
        });
    }*/

    /**private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputUsername.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if(binding.inputUsername.getText().toString().trim().isEmpty()) {
            toast = Toast.makeText(getApplicationContext(), "way sud username", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputUsername.getText().toString()).matches()) {
            toast = Toast.makeText(getApplicationContext(), "sayop", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            toast = Toast.makeText(getApplicationContext(), "Uway sud password", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            return true;
        }
    }*/
}