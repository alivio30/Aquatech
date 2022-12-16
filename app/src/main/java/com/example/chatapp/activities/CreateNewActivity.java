package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.fragments.NewConsumerFragment;
import com.example.chatapp.fragments.NewMeterNAdminFragment;

import java.util.ArrayList;
import java.util.List;

public class CreateNewActivity extends AppCompatActivity{

    Spinner spinner;

    NewMeterNAdminFragment newMeterNAdminFragment;
    NewConsumerFragment newConsumerFragment;
    List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);

        Spinner spinner = findViewById(R.id.spinnerPosition);
        newMeterNAdminFragment = new NewMeterNAdminFragment();
        newConsumerFragment = new NewConsumerFragment();

        names = new ArrayList<>();
        names.add("Consumer");
        names.add("Meter Reader");
        names.add("Admin");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner,
                names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        setFragment(newConsumerFragment);
                        break;
                    case 1:
                    case 2:
                        setFragment(newMeterNAdminFragment);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}