package com.example.chatapp.ActivityEmpPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.ConsumerDetails;
import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;

public class ReaderProfileDetails extends AppCompatActivity {
    ConsumerDetails consumerDetails = new ConsumerDetails();
    ImageView consumerProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_profile_details);

        consumerProfile = findViewById(R.id.imageProfile);
        consumerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, consumerDetails).commit();
            }
        });
    }


}