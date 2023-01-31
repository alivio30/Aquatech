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

import com.example.chatapp.FragmentEmpPage.RegAdmin;
import com.example.chatapp.FragmentEmpPage.RegConsumer;
import com.example.chatapp.FragmentEmpPage.RegMeterReader;
import com.example.chatapp.R;

public class CreateUser extends AppCompatActivity {

    Button createConsumer, createMeterReader, createAdmin;
    RegConsumer consumer = new RegConsumer();
    RegMeterReader meterReader = new RegMeterReader();
    RegAdmin admin = new RegAdmin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        TextView userAccounts = findViewById(R.id.textviewCreateUserAccount);
        createConsumer = findViewById(R.id.buttonConsumer);
        createMeterReader = findViewById(R.id.buttonMeterReader);
        createAdmin = findViewById(R.id.buttonAdmin);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FrameLayout layout = findViewById(R.id.FragmentContainer);

        createConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.FragmentContainer,consumer).addToBackStack(null).commit();
                userAccounts.setVisibility(View.GONE);
                createConsumer.setVisibility(View.GONE);
                createMeterReader.setVisibility(View.GONE);
                createAdmin.setVisibility(View.GONE);
            }
        });

        createMeterReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.FragmentContainer,meterReader).addToBackStack(null).commit();
                userAccounts.setVisibility(View.GONE);
                createConsumer.setVisibility(View.GONE);
                createMeterReader.setVisibility(View.GONE);
                createAdmin.setVisibility(View.GONE);
            }
        });

        createAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.FragmentContainer,admin).addToBackStack(null).commit();
                userAccounts.setVisibility(View.GONE);
                createConsumer.setVisibility(View.GONE);
                createMeterReader.setVisibility(View.GONE);
                createAdmin.setVisibility(View.GONE);
            }
        });
    }
}