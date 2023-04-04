package com.example.chatapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.consumerPage.CMasterPage;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {
    View view;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        username = view.findViewById(R.id.inputUsername);
        password = view.findViewById(R.id.inputPassword);
        button = view.findViewById(R.id.buttonSignIn);
        forgotPass = view.findViewById(R.id.textForgotPassword);
        progressBar = view.findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emptyFields = "Please input necessary field/s.";
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), emptyFields, Toast.LENGTH_SHORT).show();
                }else{
                    signIn();
                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, forgotPassword).addToBackStack(null).commit();
            }
        });

        preferenceManager = new PreferenceManager(getContext());
        return view;
    }

    public void signIn() {
        UserDetails userDetails = new UserDetails();
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility((View.INVISIBLE));
        db.collection("users")
                .whereEqualTo("userName", username.getText().toString())
                .whereEqualTo("password", password.getText().toString())
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful() && userTask.getResult() != null && userTask.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = userTask.getResult().getDocuments().get(0);
                        if(documentUserSnapshot.getString("userType").equalsIgnoreCase("Super Admin")){
                            Toast.makeText(getContext(), "Incorrect username and password.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            button.setVisibility((View.VISIBLE));
                            //clearFields();
                        }else{
                            if(documentUserSnapshot.getString("status").equalsIgnoreCase("Active")){
                                db.collection("companyDetails").whereEqualTo("companyId", documentUserSnapshot.getString("companyId"))
                                        .get()
                                        .addOnCompleteListener(companyTask -> {
                                            if (companyTask.isSuccessful() && companyTask.getResult() != null && companyTask.getResult().getDocuments().size() > 0) {
                                                DocumentSnapshot documentUserSnapshot1 = companyTask.getResult().getDocuments().get(0);
                                                if(documentUserSnapshot1.getString("status").equalsIgnoreCase("Active")){
                                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                                    preferenceManager.putString(Constants.KEY_USER_ID, documentUserSnapshot.getString("userId"));
                                                    preferenceManager.putString(Constants.KEY_NAME, documentUserSnapshot.getString(Constants.KEY_NAME));
                                                    preferenceManager.putString(Constants.KEY_IMAGE, documentUserSnapshot.getString(Constants.KEY_IMAGE));
                                                    preferenceManager.putString(Constants.KEY_USER_TYPE, documentUserSnapshot.getString(Constants.KEY_USER_TYPE));

                                                    userDetails.setCompanyID(documentUserSnapshot.getString("companyId"));
                                                    userDetails.setName(documentUserSnapshot.getString("name"));
                                                    userDetails.setUsername(documentUserSnapshot.getString("userName"));
                                                    userDetails.setPassword(documentUserSnapshot.getString("password"));
                                                    userDetails.setAddress(documentUserSnapshot.getString("address"));
                                                    userDetails.setEmail(documentUserSnapshot.getString("email"));
                                                    userDetails.setContactNumber(documentUserSnapshot.getString("contactNumber"));
                                                    userDetails.setUserType(documentUserSnapshot.getString("userType"));
                                                    userDetails.setUserID(documentUserSnapshot.getString("userId"));
                                                    userDetails.setImage(documentUserSnapshot.getString("image"));
                                                    userDetails.setCompanyName(documentUserSnapshot1.getString("companyName"));

                                                    progressBar.setVisibility(View.GONE);
                                                    button.setVisibility((View.VISIBLE));
                                                    if (userDetails.getUserType().equalsIgnoreCase("admin")) {
                                                        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                                            Intent intent = new Intent(this.getContext(), MasterPage.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            //finish();
                                                        }
                                                    }
                                                    if (userDetails.getUserType().equalsIgnoreCase("meter reader")) {
                                                        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                                            Intent intent = new Intent(this.getContext(), MasterPage.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            //finish();
                                                        }
                                                    }
                                                    if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
                                                        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                                            Intent intent = new Intent(this.getContext(), CMasterPage.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            //finish();
                                                        }
                                                    }

                                                }else{
                                                    Toast.makeText(getContext(), "Company is inactive. Please contact the company", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    button.setVisibility((View.VISIBLE));
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(getContext(), "Account is inactive. Please contact the admin.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                button.setVisibility((View.VISIBLE));
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility((View.VISIBLE));
                        toast = Toast.makeText(getContext(), "Incorrect username and password.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    public void clearFields(){
        username.setText(null);
        password.setText(null);
    }
}