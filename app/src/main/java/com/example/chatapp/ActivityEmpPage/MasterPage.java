package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.FragmentEmpPage.HomePage;
import com.example.chatapp.FragmentEmpPage.ProfilePage;
import com.example.chatapp.FragmentEmpPage.ReaderProfileFragment;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.example.chatapp.activities.MainActivity;
import com.example.chatapp.activities.UsersActivity;
import com.example.chatapp.databinding.ActivityMasterPageBinding;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

public class MasterPage extends AppCompatActivity {
    ActivityMasterPageBinding binding;
    BottomNavigationView bottomNavigationView;
    ImageView chatIcon, profile;
    TextView userType;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    HomePage homePage = new HomePage();
    SearchPage searchPage = new SearchPage();
    ProfilePage adminProfilePage = new ProfilePage();
    ReaderProfileFragment readerProfilePage = new ReaderProfileFragment();
    UserDetails userDetails = new UserDetails();
    Toast toast;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterPageBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_master_page);
        userType = findViewById(R.id.textPosition);
        profile = findViewById(R.id.imageProfile);

        //display image profile
        //toast = Toast.makeText(getApplicationContext(), userDetails.getImage(), Toast.LENGTH_SHORT);
        //toast.show(
        String url = userDetails.getImage();
        new fetchImage(url).start();
        //set text if user is reader or admin
        if(userDetails.getUserType().equalsIgnoreCase("admin")) userType.setText("Admin");
        if(userDetails.getUserType().equalsIgnoreCase("meter reader")) userType.setText("Meter Reader");
        //chat icon
        chatIcon = findViewById(R.id.imageMessenger);
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(intent);
            }
        });

        //navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homePage).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(integerDeque.contains(id)){
                    if(id == R.id.home){
                        if(integerDeque.size() != 1){
                            if(flag){
                                integerDeque.addFirst(R.id.home);
                                flag = false;
                            }
                        }
                    }
                    //integerDeque.remove(id);
                }
                integerDeque.push(id);
                loadFragment(getFragment(item.getItemId()));
                return false;
            }
        });
        /**bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homebar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homePage).addToBackStack(null).commit();
                        return true;
                    case R.id.searchbar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, searchPage).addToBackStack(null).commit();
                        return true;
                    case R.id.profilebar:
                        if(userDetails.getUserType().equalsIgnoreCase("meter reader")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, readerProfilePage).addToBackStack(null).commit();
                        }
                        if(userDetails.getUserType().equalsIgnoreCase("admin")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, adminProfilePage).addToBackStack(null).commit();
                        }
                        //getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, profilePage).addToBackStack(null).commit();
                        return true;
                }
                return false;
            }
        });*/
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
                    progressDialog = new ProgressDialog(MasterPage.this);
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
            case R.id.homebar:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomePage();
            case R.id.searchbar:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new SearchPage();
            case R.id.profilebar:
                if(userDetails.getUserType().equalsIgnoreCase("meter reader")){
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    return new ReaderProfileFragment();
                }
                if(userDetails.getUserType().equalsIgnoreCase("admin")){
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    return new ProfilePage();
                }
        }
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        return new HomePage();
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
        }/**else{
            finish();
        }*/
    }
}