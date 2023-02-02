package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.CreateUserFragment;
import com.example.chatapp.FragmentEmpPage.HomePage;
import com.example.chatapp.FragmentEmpPage.ProfilePage;
import com.example.chatapp.FragmentEmpPage.RegAdmin;
import com.example.chatapp.FragmentEmpPage.RegConsumer;
import com.example.chatapp.FragmentEmpPage.RegMeterReader;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.example.chatapp.consumerPage.CMasterPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CreateUser extends AppCompatActivity {
    CreateUserFragment createUser = new CreateUserFragment();
    //BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfilePage adminProfile = new ProfilePage();
        SearchPage searchPage = new SearchPage();
        HomePage homePage = new HomePage();
        setContentView(R.layout.activity_create_user);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FrameLayout layout = findViewById(R.id.FragmentContainer);
        fragmentTransaction.replace(R.id.FragmentContainer,createUser).commit();

        /**navigationView = findViewById(R.id.bottomNavigationView);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homebar:
                        Intent intent = new Intent(getApplicationContext(), MasterPage.class);
                        startActivity(intent);
                        return true;
                    case R.id.searchbar:
                        onBackPressed();
                        //fragmentTransaction.replace(R.id.FragmentContainer, searchPage).addToBackStack(null).commit();
                         return true;
                    case R.id.profilebar:
                        onBackPressed();
                        //fragmentTransaction.replace(R.id.FragmentContainer, adminProfile).addToBackStack(null).commit();
                        return true;
                }
                return false;
            }
        });*/

    }
}