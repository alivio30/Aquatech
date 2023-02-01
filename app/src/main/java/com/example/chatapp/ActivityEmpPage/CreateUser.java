package com.example.chatapp.ActivityEmpPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.CreateUserFragment;
import com.example.chatapp.FragmentEmpPage.RegAdmin;
import com.example.chatapp.FragmentEmpPage.RegConsumer;
import com.example.chatapp.FragmentEmpPage.RegMeterReader;
import com.example.chatapp.R;

public class CreateUser extends AppCompatActivity {
    CreateUserFragment createUser = new CreateUserFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FrameLayout layout = findViewById(R.id.FragmentContainer);
        fragmentTransaction.replace(R.id.FragmentContainer,createUser).addToBackStack(null).commit();


    }
}