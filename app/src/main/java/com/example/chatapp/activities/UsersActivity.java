package com.example.chatapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatapp.R;
import com.example.chatapp.adapters.UsersAdapter;
import com.example.chatapp.databinding.ActivityUsersBinding;
import com.example.chatapp.listeners.UserListener;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.Logout;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    UserDetails userDetails = new UserDetails();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.textPosition.setText(preferenceManager.getString(Constants.KEY_USER_TYPE));
        String imageUrl = null;
        imageUrl = preferenceManager.getString(Constants.KEY_IMAGE);
        Picasso.get().load(imageUrl).into(binding.imageProfile);

        setListener();
        if(userDetails.getUserType().equalsIgnoreCase("admin")){
            getUsers();
        }else if(userDetails.getUserType().equalsIgnoreCase("consumer") || userDetails.getUserType().equalsIgnoreCase("meter reader")){
            getAdmin();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setListener() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getAdmin(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("userType", "Admin")
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.type = queryDocumentSnapshot.getString("userType");
                            user.id = queryDocumentSnapshot.getString("userId");
                            //user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter((usersAdapter));
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }else {
                            showErrorMessage();
                        }
                    }else {
                        showErrorMessage();
                    }
                });
    }
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                         for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                             if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                 continue;
                             }
                             User user = new User();
                             user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                             user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                             user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                             user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                             user.id = queryDocumentSnapshot.getString("userId");
                             //user.id = queryDocumentSnapshot.getId();
                             users.add(user);
                         }
                         if (users.size() > 0) {
                             UsersAdapter usersAdapter = new UsersAdapter(users, this);
                             binding.usersRecyclerView.setAdapter((usersAdapter));
                             binding.usersRecyclerView.setVisibility(View.VISIBLE);
                         }else {
                             showErrorMessage();
                         }
                    }else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText((String.format("%s", "No user available")));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}