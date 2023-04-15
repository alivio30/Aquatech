package com.example.chatapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class BaseActivity extends AppCompatActivity {

    UserDetails userDetails = new UserDetails();
    private DocumentReference documentReference;
    FirebaseFirestore database= FirebaseFirestore.getInstance();
    private DocumentSnapshot documentSnapshot;
    private String documentID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            documentSnapshot = task.getResult().getDocuments().get(0);
                            documentID = documentSnapshot.getId();
                        }
                    }
                });*/
    }
    //set user availability to 0 if the device is on onPause
    @Override
    protected void onPause() {
        super.onPause();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            documentSnapshot = task.getResult().getDocuments().get(0);
                            documentID = documentSnapshot.getId();
                            database.collection("users")
                                    .document(documentID)
                                    .update(Constants.KEY_AVAILABILITY, 0);
                        }
                    }
                });
    }

    //set user availability to 1 if the device is on onResume
    @Override
    protected void onResume() {
        super.onResume();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("userId", userDetails.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            documentSnapshot = task.getResult().getDocuments().get(0);
                            documentID = documentSnapshot.getId();
                            database.collection("users")
                                    .document(documentID)
                                    .update(Constants.KEY_AVAILABILITY, 1);
                        }
                    }
                });
    }
}
