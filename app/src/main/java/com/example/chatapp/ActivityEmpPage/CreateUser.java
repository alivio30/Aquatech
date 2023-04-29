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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.CreateUserFragment;
import com.example.chatapp.FragmentEmpPage.HomePage;
import com.example.chatapp.FragmentEmpPage.ProfilePage;
import com.example.chatapp.FragmentEmpPage.RegAdmin;
import com.example.chatapp.FragmentEmpPage.RegConsumer;
import com.example.chatapp.FragmentEmpPage.RegMeterReader;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.example.chatapp.activities.MainActivity;
import com.example.chatapp.consumerPage.CMasterPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CreateUser extends AppCompatActivity {
    CreateUserFragment createUser = new CreateUserFragment();
    ImageView chatImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        chatImage = findViewById(R.id.imageMessenger);

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //display create user fragment
        fragmentTransaction.replace(R.id.FragmentContainer,createUser).commit();

    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}