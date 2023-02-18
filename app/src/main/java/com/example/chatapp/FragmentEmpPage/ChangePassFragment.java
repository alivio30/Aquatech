package com.example.chatapp.FragmentEmpPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.chatapp.consumerPage.ConsumerProfileFragment;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChangePassFragment extends Fragment {
    UserDetails userDetails = new UserDetails();
    View view;
    ImageView backButton;
    EditText oldPassword, newPassword, confirmPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button changePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        backButton = view.findViewById(R.id.imageBack);
        ConsumerProfileFragment consumerProfile = new ConsumerProfileFragment();
        ProfilePage adminProfile = new ProfilePage();
        ReaderProfileFragment readerProfile = new ReaderProfileFragment();
        changePass = view.findViewById(R.id.buttonChangeNewPassword);
        oldPassword = view.findViewById(R.id.inputOldPass);
        newPassword = view.findViewById(R.id.inputNewPassword);
        confirmPassword = view.findViewById(R.id.inputConfirmPassword);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPassword.getText().toString().trim().isEmpty() || newPassword.getText().toString().trim().isEmpty() || confirmPassword.getText().toString().trim().isEmpty()){
                    showToast("Please input necessary fields.");
                }else if(!oldPassword.getText().toString().equals(userDetails.getPassword())){
                    showToast("Old password doesn't match on your account.");
                }else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    showToast("Password not matched.");
                }else{
                    detailsValidator();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                if(userDetails.getUserType().equalsIgnoreCase("consumer")){
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.FragmentContainer, consumerProfile);
                    transaction.commit();
                }
                if(userDetails.getUserType().equalsIgnoreCase("meter reader")){
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.FragmentContainer, adminProfile);
                    transaction.commit();
                }
                if(userDetails.getUserType().equalsIgnoreCase("admin")){
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.FragmentContainer, adminProfile);
                    transaction.commit();
                }
            }
        });
        return view;
    }
    public void detailsValidator(){
        Map<String, Object> changePassword = new HashMap<>();
        changePassword.put("password", newPassword.getText().toString());

        db.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .whereEqualTo("password", oldPassword.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("users")
                                    .document(documentID)
                                    .update(changePassword)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            userDetails.setPassword(newPassword.getText().toString());
                                            showToast("Your password has been updated!");
                                            clearFields();
                                        }
                                    });
                        }
                    }
                });
    }
    private void clearFields(){
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}