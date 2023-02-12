package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.FragmentEmpPage.ConsumerDetails;
import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.UserDetailsRecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReaderProfileDetails extends AppCompatActivity {
    TextView txtPreviousReading, txtInputPresentReading, txtWaterConsumption, txtaddres, txtname, txtaccountNumber, txtserialNumber, txtpumpNumber, txttankNumber, txtlineNumber, txtmeterStandNumber;
    String consId, mail, number, name, accountNumber, meterStandNumber, pumpNumber, tankNumber, meterSerialNumber, lineNumber, image, address;
    ImageView imageProfile, scannedMeter, previousScannedMeter;
    Button scanButton, submitButton;
    Uri imageUri;
    Bitmap bitmap;
    Properties properties = new Properties();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    Toast toast;
    ProgressDialog progressDialog;
    Calendar calendar = Calendar.getInstance();
    int year, month, day;
    String billingNumber;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    int billingID, watercharge=10;
    double tax =0;
    double billAmountInvoice=0, billAmount=0, netAmount=100, credit=0, penalty=0, discount=0, previousBalance=0, reconnectionFee=0, others=0;
    String meterImage, bill_no, readingDate, status, startBillingPeriod, endBillingPeriod, dueDate;
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
        consId = getIntent().getStringExtra("consID");

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
        txtWaterConsumption = findViewById(R.id.textWaterConsumption);
        txtInputPresentReading = findViewById(R.id.InputPresentReading);
        txtPreviousReading = findViewById(R.id.textPreviousReading);
        previousScannedMeter = findViewById(R.id.imagePrevWaterMeter);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
        validation();
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
                if(txtInputPresentReading.getText().toString().trim().isEmpty() || scannedMeter.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "Please input necessary fields", Toast.LENGTH_SHORT).show();
                }else{
                    calculateBill();
                    onBackPressed();
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void validation(){
        Date now = new Date();
        db.collection("billing")
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        if(documentUserSnapshot.getString("dueDate").equals(sdf.format(now))){
                            db.collection("consumers").whereEqualTo("consId", consId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                                            if(documentUserSnapshot.getString("remarks").equalsIgnoreCase("unread")){
                                                scanButton.setClickable(true);
                                                submitButton.setClickable(true);
                                                txtInputPresentReading.setEnabled(true);
                                                txtPreviousReading.setText(documentUserSnapshot.getString("presentReading"));
                                                StringToBitMap(previousScannedMeter, documentUserSnapshot.getString("meterImage"));
                                            }
                                        }
                                    }
                                });
                            db.collection("consumers")
                                    .whereEqualTo("consId", consId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                                                DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                                                String documentId = documentUserSnapshot.getId();
                                                db.collection("consumers")
                                                        .document(documentId)
                                                        .update("remarks", "unread");
                                            }
                                        }
                                    });
                        }else{
                            scanButton.setClickable(false);
                            submitButton.setClickable(false);
                            txtInputPresentReading.setEnabled(false);
                            scanButton.setBackgroundColor(Color.rgb(255, 0, 0));
                            submitButton.setBackgroundColor(Color.rgb(255, 0, 0));
                            txtInputPresentReading.setText(documentUserSnapshot.getString("presentReading"));
                            StringToBitMap(scannedMeter, documentUserSnapshot.getString("meterImage"));
                        }
                    }
                })
                .addOnFailureListener(task ->{
                    scanButton.setClickable(true);
                });
    }

    public int billingID() {
        Random userRandom = new Random();
        return ((1 + userRandom.nextInt(9)) * 10000 + userRandom.nextInt(10000));
    }
    public int generateBillingNumber(){
        Random billingNumberRandom = new Random();
        return ((1 + billingNumberRandom.nextInt(9)) * 10000 + billingNumberRandom.nextInt(10000));
    }
    public Date getDueDate(int date){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, date);

        return cal.getTime();
    }

    public void calculateBill(){
        billingID = billingID();
        billingNumber = Integer.toString(year)+ "" +Integer.toString(month)+ "" +generateBillingNumber();
        Date now = new Date();

        billAmount = Integer.parseInt(txtWaterConsumption.getText().toString()) * watercharge;
        readingDate = sdf.format(now);
        tax = billAmount * 0.12;
        status = "Pending";
        dueDate = sdf.format(getDueDate(15));
        meterImage = BitMapToString(bitmap);
        netAmount = (billAmount + 0 + 0 + 0 + 0) - (0+0); //
        startBillingPeriod = sdf.format(getDueDate(-30));
        endBillingPeriod = sdf.format(getDueDate(-1));
        credit = 0; //excessPaid
        penalty = 0; //penalty
        discount = 0; // discount
        previousBalance = 0 - 0; // billAmount - paidAmount
        reconnectionFee = 0; // reconnectionFee
        others = 0; // others
        billAmountInvoice = 0 + 0 + 0;// billAmountInvoice + reconnectionFee + others;

        //Toast.makeText(getApplicationContext(), image, Toast.LENGTH_SHORT).show();
        Map<String, Object> createBilling = new HashMap<>();
        createBilling.put("billingId", String.valueOf(billingID));
        createBilling.put("consId", consId);
        createBilling.put("invoiceId", "null");
        createBilling.put("bill_no", billingNumber);
        createBilling.put("readingDate", readingDate);
        createBilling.put("presentReading", txtInputPresentReading.getText().toString());
        createBilling.put("previousReading", txtPreviousReading.getText().toString());
        createBilling.put("ConsumptionUnit", txtWaterConsumption.getText().toString());
        createBilling.put("waterCharge", watercharge);
        createBilling.put("tax", tax);
        createBilling.put("billAmount", billAmount);
        createBilling.put("others", others);
        createBilling.put("previousBalance", previousBalance);
        createBilling.put("reconnectionFee", reconnectionFee);
        createBilling.put("penalty", penalty);
        createBilling.put("discount", discount);
        createBilling.put("credit", credit);
        createBilling.put("netAmount", netAmount);
        createBilling.put("dueDate", dueDate);
        createBilling.put("meterImage", meterImage);
        createBilling.put("status", status);
        if(Integer.parseInt(txtWaterConsumption.getText().toString()) < 0){
            toast = Toast.makeText(getApplicationContext(), "Water Consumption went to negative!", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            //add to billing table
            db.collection("billing")
                    .add(createBilling)
                    .addOnSuccessListener(documentReference -> {
                        toast = Toast.makeText(getApplicationContext(), "Saved to billing!", Toast.LENGTH_SHORT);
                        toast.show();
                        db.collection("consumers")
                                .whereEqualTo("consId", consId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("consumers")
                                                    .document(documentID)
                                                    .update("remarks", "read")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            notifyBill();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    })
                    .addOnFailureListener(exception -> {
                        toast = Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT);
                        toast.show();
                    });
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCameraCode){
            bitmap = (Bitmap)data.getExtras().get("data");
            scannedMeter.setImageBitmap(bitmap);
            int consumption = Integer.parseInt(txtInputPresentReading.getText().toString()) - Integer.parseInt(txtPreviousReading.getText().toString());
            txtWaterConsumption.setText(String.valueOf(consumption));
        }
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void StringToBitMap(ImageView bitImage, String image){
        byte[] imageBytes;
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        bitImage.setImageBitmap(bitmap);
    }

    /**public void retrievePreviousReading(){
        db.collection("billing")
                .whereEqualTo("consId", getIntent().getStringExtra("consID"))
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        txtPreviousReading.setText(documentUserSnapshot.getString("previousReading"));
                        StringToBitMap(documentUserSnapshot.getString("meterImage"));
                        txtWaterConsumption.setText(documentUserSnapshot.getString("ConsumptionUnit"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }*/
}