package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.FragmentEmpPage.ConsumerDetails;
import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReaderProfileDetails extends AppCompatActivity {
    TextView txtaddres, txtname, txtaccountNumber, txtserialNumber, txtpumpNumber, txttankNumber, txtlineNumber, txtmeterStandNumber;
    String mail, number, name, accountNumber, meterStandNumber, pumpNumber, tankNumber, meterSerialNumber, lineNumber, image, address;
    ImageView imageProfile, scannedMeter;
    Button scanButton, submitButton;
    Properties properties = new Properties();
    public static final int requestCameraCode = 12;
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
        number = getIntent().getStringExtra("contactNumber");
        mail = getIntent().getStringExtra("mail");

        imageProfile = findViewById(R.id.imageProfile);
        txtname = findViewById(R.id.textProfileName);
        txtaccountNumber = findViewById(R.id.textAccountNumber);
        txtserialNumber = findViewById(R.id.textSerialNumber);
        txtpumpNumber = findViewById(R.id.textPumpNumber);
        txttankNumber = findViewById(R.id.textTankNumber);
        txtlineNumber = findViewById(R.id.textLineNumber);
        txtmeterStandNumber = findViewById(R.id.textMeterStand);
        txtaddres = findViewById(R.id.textAddress);
        scanButton = findViewById(R.id.buttonScan);
        scannedMeter = findViewById(R.id.imagePresWaterMeter);
        submitButton = findViewById(R.id.buttonSubmit);

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

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, requestCameraCode);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyBill();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void notifyBill(){
        //Send to SMS
        if(ContextCompat.checkSelfPermission(ReaderProfileDetails.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){
            sendSMS();

        }else{
            ActivityCompat.requestPermissions(ReaderProfileDetails.this, new String[]{
                    Manifest.permission.SEND_SMS}, 100);
        }
        //Send to email
        final String username = "crackadood095@gmail.com";
        final String password = "cqnbyusawaqmjoui";
        String messageToSend = "Hello, "+name+"! Your new bill is set!";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username, password);
            }
        });
        try{
            Message message  = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject("Sending message to receiver through app");
            message.setText(messageToSend);
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "email send successfull", Toast.LENGTH_SHORT).show();
        } catch (AddressException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSMS();
        }else{
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(){
        String phone = number;
        String message = "Hello, "+name+"! Your new bill is set!";

        if(!phone.isEmpty() && !message.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(this, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Phone number not existed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCameraCode){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            scannedMeter.setImageBitmap(bitmap);
        }
    }
}