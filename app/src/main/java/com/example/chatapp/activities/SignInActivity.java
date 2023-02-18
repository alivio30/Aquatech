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
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.consumerPage.CMasterPage;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.example.chatapp.firebase.MessagingService;
import com.example.chatapp.fragments.ForgotPassword;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    ForgotPassword forgotPassword = new ForgotPassword();
    EditText username, password;
    TextView forgotPass;
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
        forgotPass = findViewById(R.id.textForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    signIn();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, forgotPassword).addToBackStack(null).commit();
            }
        });

        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    public void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility((View.INVISIBLE));
        UserDetails userDetails = new UserDetails();
        db.collection("users")
                .whereEqualTo("userName", username.getText().toString())
                .whereEqualTo("password", password.getText().toString())
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful() && userTask.getResult() != null && userTask.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = userTask.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentUserSnapshot.getString("userId"));
                        preferenceManager.putString(Constants.KEY_NAME, documentUserSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentUserSnapshot.getString(Constants.KEY_IMAGE));
                        preferenceManager.putString(Constants.KEY_USER_TYPE, documentUserSnapshot.getString(Constants.KEY_USER_TYPE));

                        userDetails.setName(documentUserSnapshot.getString("name"));
                        userDetails.setUsername(documentUserSnapshot.getString("userName"));
                        userDetails.setPassword(documentUserSnapshot.getString("password"));
                        userDetails.setAddress(documentUserSnapshot.getString("address"));
                        userDetails.setEmail(documentUserSnapshot.getString("email"));
                        userDetails.setContactNumber(documentUserSnapshot.getString("contactNumber"));
                        userDetails.setUserType(documentUserSnapshot.getString("userType"));
                        userDetails.setUserID(documentUserSnapshot.getString("userId"));
                        userDetails.setImage(documentUserSnapshot.getString("image"));

                        progressBar.setVisibility(View.GONE);
                        button.setVisibility((View.VISIBLE));
                        if (userDetails.getUserType().equalsIgnoreCase("admin")) {
                            if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                Intent intent = new Intent(this, MasterPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        if (userDetails.getUserType().equalsIgnoreCase("meter reader")) {
                            if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                Intent intent = new Intent(this, MasterPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
                            if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                Intent intent = new Intent(this, CMasterPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        //clearFields();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility((View.VISIBLE));
                        toast = Toast.makeText(getApplicationContext(), "Incorrect username and password.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    public void clearFields(){
        username.setText(null);
        password.setText(null);
    }
}