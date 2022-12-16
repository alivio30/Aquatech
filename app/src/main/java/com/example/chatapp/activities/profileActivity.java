package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityHomeBinding;
import com.example.chatapp.databinding.ActivityProfileBinding;
import com.example.chatapp.fragments.ChangePassFragment;
import com.example.chatapp.fragments.CreateAccountFragment;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class profileActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    ActivityProfileBinding binding;
    private FirebaseFirestore database;
    Intent intent;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();

        binding.bottomNavigationView.setSelectedItemId(R.id.profile);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), homeActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), searchActivity.class));
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile:
                    return true;
            }
            return false;
        });
        btn = findViewById(R.id.buttonChangePassword);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ChangePassFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment).commit();
                binding.buttonChangePassword.setVisibility(View.GONE);
                binding.buttonCreateNewAccount.setVisibility(View.GONE);
            }
        });
    }
    private  void setListener() {
        binding.buttonCreateNewAccount.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), CreateNewActivity.class)));
        binding.imageSignOut.setOnClickListener(view -> signOut());
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }
}