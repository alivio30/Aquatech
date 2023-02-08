package com.example.chatapp.consumerPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.FragmentEmpPage.AdminBillingDetails;
import com.example.chatapp.R;
import com.example.chatapp.activities.MainActivity;
import com.example.chatapp.activities.UsersActivity;
import com.example.chatapp.activities.homeActivity;
import com.example.chatapp.activities.profileActivity;
import com.example.chatapp.activities.searchActivity;
import com.example.chatapp.databinding.ActivityHomeBinding;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class CMasterPage extends AppCompatActivity {
    AdminBillingDetails consumerHome = new AdminBillingDetails();
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    UserDetails userDetails = new UserDetails();
    BottomNavigationView navigationView;
    ImageView chatIcon, profile;
    Toast toast;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Deque<Integer> integerDeque = new ArrayDeque<>(2);
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmaster_page2);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerHome).commit();
        chatIcon = findViewById(R.id.imageMessenger);
        profile = findViewById(R.id.imageProfile);
        setConsumerData();
        //display image from url
        String url = userDetails.getImage();
        new fetchImage(url).start();

        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(integerDeque.contains(id)){
                    if(id == R.id.chome){
                        if(integerDeque.size() != 1){
                            if(flag){
                                integerDeque.addFirst(R.id.chome);
                                flag = false;
                            }
                        }
                    }
                    integerDeque.remove(id);
                }
                integerDeque.push(id);
                loadFragment(getFragment(item.getItemId()));
                return false;
            }
        });
    }

    public void setConsumerData(){
        db.collection("consumers")
                .whereEqualTo("userId", userDetails.getConsumerID())
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
                        toast = Toast.makeText(getApplicationContext(), "consId: "+userDetails.getConsumerID(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }
    public class fetchImage extends Thread{
        String URL;
        Bitmap bitmap;
        fetchImage(String URL){
            this.URL = URL;
        }
        @Override
        public void run(){
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(CMasterPage.this);
                    progressDialog.setMessage("Fetching url..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            try {
                InputStream inputStream = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    profile.setImageBitmap(bitmap);
                }
            });
        }
    }
    //for navigation bar
    private Fragment getFragment(int itemId) {
        switch(itemId){
            case R.id.chome:
                navigationView.getMenu().getItem(0).setChecked(true);
                return new ChomeFragment();
            case R.id.cprofile:
                navigationView.getMenu().getItem(1).setChecked(true);
                return new ConsumerProfileFragment();
        }
        navigationView.getMenu().getItem(1).setChecked(true);
        return new ChomeFragment();
    }
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
    @Override
    public void onBackPressed(){
        integerDeque.pop();
        if(!integerDeque.isEmpty()){
            loadFragment(getFragment(integerDeque.peek()));
        }else{
            finish();
        }
    }
}