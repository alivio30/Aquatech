package com.example.chatapp.consumerPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chatapp.R;
import com.example.chatapp.activities.MainActivity;
import com.example.chatapp.activities.UsersActivity;
import com.example.chatapp.activities.homeActivity;
import com.example.chatapp.activities.profileActivity;
import com.example.chatapp.activities.searchActivity;
import com.example.chatapp.databinding.ActivityHomeBinding;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class CMasterPage extends AppCompatActivity {
    ChomeFragment consumerHome = new ChomeFragment();
    ConsumerProfileFragment consumerProfileFragment = new ConsumerProfileFragment();
    BottomNavigationView navigationView;
    ImageView chatIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmaster_page2);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerHome).commit();
        chatIcon = findViewById(R.id.imageMessenger);
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(intent);
            }
        });
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerHome).addToBackStack(null).commit();
                        return true;
                    case R.id.cprofile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerProfileFragment).addToBackStack(null).commit();
                        return true;
                }
                return false;
            }
        });
    }
}