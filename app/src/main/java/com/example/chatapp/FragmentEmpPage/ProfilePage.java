package com.example.chatapp.FragmentEmpPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.CreateUser;
import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.fragments.ForgotPassword;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.Logout;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ProfilePage extends Fragment {
    View view;
    UserDetails userDetails = new UserDetails();
    Button changePassword, createNewAccount;
    ImageView logoutButton;
    Logout logout = new Logout();
    ChangePassFragment changePassFragment = new ChangePassFragment();
    TextView userId, address, contactNumber, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile_page, container, false);
        logoutButton = view.findViewById(R.id.imageLogout);
        changePassword = view.findViewById(R.id.buttonChangePassword);
        createNewAccount = view.findViewById(R.id.buttonCreateNewAccount);
        //CreateUserFragment createUserFragment = new CreateUserFragment();
        userId = view.findViewById(R.id.textEmployeeId);
        address = view.findViewById(R.id.textAddress);
        contactNumber = view.findViewById(R.id.textContactNumber);
        email = view.findViewById(R.id.textEmailAddress);
        displayData();
        Toast.makeText(getContext(), userDetails.getUserID(), Toast.LENGTH_SHORT).show();

        //change password
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, changePassFragment);
                transaction.commit();
            }
        });

        //create user account
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateUser.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eraseToken();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    public void displayData(){
        db.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        userId.setText(documentUserSnapshot.getString("userId"));
                        address.setText(documentUserSnapshot.getString("address"));
                        contactNumber.setText(documentUserSnapshot.getString("contactNumber"));
                        email.setText(documentUserSnapshot.getString("email"));
                    }
                });
    }
    public void eraseToken(){
        Toast.makeText(getContext(), userDetails.getUserID(), Toast.LENGTH_SHORT).show();
        db.collection("users")
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            HashMap<String, Object> clearToken = new HashMap<>();
                            clearToken.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                            db.collection("users")
                                    .document(documentID)
                                    .update(clearToken)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Signing out...", Toast.LENGTH_SHORT).show();
                                            logout.clearAllData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Unable to sign out", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }
}