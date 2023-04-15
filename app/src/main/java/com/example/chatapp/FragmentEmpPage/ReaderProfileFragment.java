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

import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
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


public class ReaderProfileFragment extends Fragment {
    View view;
    Button buttonChangePassword;
    ImageView logout;
    Logout userLogout = new Logout();
    TextView userId, address, contactNumber, email;
    ChangePassFragment changePassFragment = new ChangePassFragment();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserDetails userDetails = new UserDetails();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reader_profile, container, false);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        userId = view.findViewById(R.id.textEmployeeId);
        contactNumber = view.findViewById(R.id.textContactNumber);
        address = view.findViewById(R.id.textAddress);
        email = view.findViewById(R.id.textEmailAddress);
        logout = view.findViewById(R.id.imageLogout);
        displayData();

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.FragmentContainer, changePassFragment);
                transaction.commit();
            }
        });
        //redirects to login activity
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eraseToken();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    //method for display user data
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
    //method for erasing token
    public void eraseToken(){
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
                                            userLogout.clearAllData();
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