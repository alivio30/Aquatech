package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.HomePage;
import com.example.chatapp.FragmentEmpPage.ProfilePage;
import com.example.chatapp.FragmentEmpPage.ReaderProfileFragment;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.example.chatapp.activities.UsersActivity;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MasterPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView chatIcon;
    TextView userType;

    HomePage homePage = new HomePage();
    SearchPage searchPage = new SearchPage();
    ProfilePage adminProfilePage = new ProfilePage();
    ReaderProfileFragment readerProfilePage = new ReaderProfileFragment();
    UserDetails userDetails = new UserDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);
        userType = findViewById(R.id.textPosition);
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
        });
    }
}