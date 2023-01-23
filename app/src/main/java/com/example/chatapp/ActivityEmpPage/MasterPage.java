package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.chatapp.FragmentEmpPage.HomePage;
import com.example.chatapp.FragmentEmpPage.ProfilePage;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MasterPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomePage homePage = new HomePage();
    SearchPage searchPage = new SearchPage();
    ProfilePage profilePage = new ProfilePage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homePage).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homebar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homePage).commit();
                        return true;
                    case R.id.searchbar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, searchPage).commit();
                        return true;
                    case R.id.profilebar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, profilePage).commit();
                        return true;
                }
                return false;
            }
        });
    }
}