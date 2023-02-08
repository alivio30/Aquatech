package com.example.chatapp.ActivityEmpPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.ConsumerDetails;
import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;
import com.squareup.picasso.Picasso;

public class ReaderProfileDetails extends AppCompatActivity {
    TextView txtaddres, txtname, txtaccountNumber, txtserialNumber, txtpumpNumber, txttankNumber, txtlineNumber, txtmeterStandNumber;
    String name, accountNumber, meterStandNumber, pumpNumber, tankNumber, meterSerialNumber, lineNumber, image, address;
    ImageView imageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_profile_details);
        name = getIntent().getStringExtra("name");
        accountNumber = getIntent().getStringExtra("accountNumber");
        meterStandNumber = getIntent().getStringExtra("meterStandNumber");
        pumpNumber = getIntent().getStringExtra("pumpNumber");
        tankNumber = getIntent().getStringExtra("tankNumber");
        meterSerialNumber = getIntent().getStringExtra("meterSerialNumber");
        lineNumber = getIntent().getStringExtra("lineNumber");
        image = getIntent().getStringExtra("image");
        address = getIntent().getStringExtra("address");

        imageProfile = findViewById(R.id.imageProfile);
        txtname = findViewById(R.id.textProfileName);
        txtaccountNumber = findViewById(R.id.textAccountNumber);
        txtserialNumber = findViewById(R.id.textSerialNumber);
        txtpumpNumber = findViewById(R.id.textPumpNumber);
        txttankNumber = findViewById(R.id.textTankNumber);
        txtlineNumber = findViewById(R.id.textLineNumber);
        txtmeterStandNumber = findViewById(R.id.textMeterStand);
        txtaddres = findViewById(R.id.textAddress);

        String imageUrl = null;
        imageUrl = image;
        Picasso.get().load(imageUrl).into(imageProfile);
        txtname.setText(name);
        txtaccountNumber.setText(accountNumber);
        txtserialNumber.setText(meterSerialNumber);
        txtpumpNumber.setText(pumpNumber);
        txttankNumber.setText(tankNumber);
        txtlineNumber.setText(lineNumber);
        txtmeterStandNumber.setText(meterStandNumber);
        txtaddres.setText(address);
    }


}