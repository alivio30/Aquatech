package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
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
import java.text.DecimalFormat;
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
    UserDetails userDetails = new UserDetails();
    AlertDialog.Builder builder;
    TextView txtPreviousReading, txtInputPresentReading, txtWaterConsumption, txtaddres, txtname, txtaccountNumber, txtserialNumber, txtpumpNumber, txttankNumber, txtlineNumber, txtmeterStandNumber;
    String consId, mail, number, name, accountNumber, meterStandNumber, pumpNumber, tankNumber, meterSerialNumber, lineNumber, image, address;
    ImageView imageProfile, scannedMeter, previousScannedMeter, imageBack;
    Button scanButton, submitButton;
    Bitmap bitmap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Toast toast;
    Calendar calendar = Calendar.getInstance();
    int year, month, day;
    String billingNumber;
    EditText inputRemarks;
    SimpleDateFormat notifyFormat = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat readingDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat filterDateFormat = new SimpleDateFormat("MMMM-yyyy");
    int billingID = 1;
    double tax =0;
    String messageDate;
    double watercharge=10.00, billAmountInvoice=0.00, billAmount=0.00, netAmount=100.00, credit=0.00, penalty=0.00, discount=0.00, previousBalance=0.00, reconnectionFee=0.00, others=0.00;
    String meterImage, bill_no, readingDate, filterDate, status, startBillingPeriod, endBillingPeriod, dueDate;
    public static final int requestCameraCode = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_profile_details);
        builder = new AlertDialog.Builder(this);
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
        imageBack = findViewById(R.id.imageBack);
        inputRemarks = findViewById(R.id.InputRemarks);

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
        billingID = billingID();
        billingNumber = Integer.toString(year)+ "" +Integer.toString(month);

        validation();
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                if(txtInputPresentReading.getText().toString().trim().isEmpty() || scannedMeter.getDrawable() == null || inputRemarks.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please input necessary fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(Integer.parseInt(txtWaterConsumption.getText().toString()) < 0){
                        builder.setTitle("Alert!")
                                .setMessage("Water Consumption went to negative!")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                    }else{
                        calculateBill();
                    }
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void getPrevReading(){
        db.collection("billing")
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double maxValue = Double.NEGATIVE_INFINITY;
                        for(DocumentSnapshot ds : task.getResult()){
                            double value = ds.getDouble("bill_no");
                            maxValue = Math.max(maxValue, value);
                        }
                        int value = (int)maxValue;
                        db.collection("billing")
                            .whereEqualTo("consId", consId)
                            .whereEqualTo("bill_no", value-1)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                                        txtPreviousReading.setText(documentUserSnapshot.getString("presentReading"));
                                        StringToBitMap(previousScannedMeter, documentUserSnapshot.getString("meterImage"));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    db.collection("consumers")
                                            .whereEqualTo("consId", consId)
                                            .get()
                                            .addOnCompleteListener(task3 ->{
                                                if (task3.isSuccessful() && task3.getResult() != null && task3.getResult().getDocuments().size() > 0) {
                                                    DocumentSnapshot documentUserSnapshot1 = task3.getResult().getDocuments().get(0);
                                                    txtPreviousReading.setText(documentUserSnapshot1.getString("firstReading"));
                                                }
                                            });
                                }
                            });

                    }
                });
    }
    public void validation(){
        Date now = new Date();
        db.collection("consumers")
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(task3 ->{
                    if (task3.isSuccessful() && task3.getResult() != null && task3.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot1 = task3.getResult().getDocuments().get(0);
                        txtPreviousReading.setText(documentUserSnapshot1.getString("firstReading"));
                    }
                });
        db.collection("billing")
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double maxValue = Double.NEGATIVE_INFINITY;
                        for(DocumentSnapshot ds : task.getResult()){
                            double value = ds.getDouble("bill_no");
                            maxValue = Math.max(maxValue, value);
                        }
                        int value = (int)maxValue;
                        db.collection("billing")
                                .whereEqualTo("consId", consId)
                                .whereEqualTo("bill_no", value)
                                .get()
                                .addOnCompleteListener(task1 ->{
                                    if (task1.isSuccessful() && task1.getResult() != null && task1.getResult().getDocuments().size() > 0) {
                                        DocumentSnapshot documentUserSnapshot = task1.getResult().getDocuments().get(0);
                                        String stringDate = documentUserSnapshot.getString("dueDate");
                                        try {
                                            Date dueDate = sdf.parse(stringDate);
                                            if(dueDate.after(now)){
                                                scanButton.setClickable(false);
                                                submitButton.setClickable(false);
                                                txtInputPresentReading.setEnabled(false);
                                                scanButton.setBackgroundColor(Color.rgb(255, 0, 0));
                                                submitButton.setBackgroundColor(Color.rgb(255, 0, 0));
                                                txtInputPresentReading.setText(documentUserSnapshot.getString("presentReading"));
                                                StringToBitMap(scannedMeter, documentUserSnapshot.getString("meterImage"));
                                                db.collection("billing")
                                                        .whereEqualTo("consId", consId)
                                                        .whereEqualTo("bill_no", value-1)
                                                        .get()
                                                        .addOnCompleteListener(task2 ->{
                                                            if (task2.isSuccessful() && task2.getResult() != null && task2.getResult().getDocuments().size() > 0) {
                                                                DocumentSnapshot documentUserSnapshot1 = task2.getResult().getDocuments().get(0);
                                                                txtPreviousReading.setText(documentUserSnapshot1.getString("presentReading"));
                                                                StringToBitMap(previousScannedMeter, documentUserSnapshot1.getString("meterImage"));
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                db.collection("consumers")
                                                                        .whereEqualTo("consId", consId)
                                                                        .get()
                                                                        .addOnCompleteListener(task3 ->{
                                                                            if (task3.isSuccessful() && task3.getResult() != null && task3.getResult().getDocuments().size() > 0) {
                                                                                DocumentSnapshot documentUserSnapshot1 = task3.getResult().getDocuments().get(0);
                                                                                txtPreviousReading.setText(documentUserSnapshot1.getString("firstReading"));
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }else{
                                                db.collection("billing")
                                                        .whereEqualTo("consId", consId)
                                                        .whereEqualTo("bill_no", value)
                                                        .get()
                                                        .addOnCompleteListener(updateTask ->{
                                                            if (updateTask.isSuccessful() && updateTask.getResult() != null && updateTask.getResult().getDocuments().size() > 0) {
                                                                DocumentSnapshot documentUserSnapshot1 = updateTask.getResult().getDocuments().get(0);
                                                                Map<String, Object> updateBilling = new HashMap<>();
                                                                updateBilling.put("meterImage", documentUserSnapshot1.getString("meterImage"));
                                                                db.collection("billing")
                                                                        .whereEqualTo("consId", consId)
                                                                        .whereEqualTo("bill_no", value)
                                                                        .get()
                                                                        .addOnCompleteListener(getIDTask ->{
                                                                            if (getIDTask.isSuccessful() && getIDTask.getResult() != null && getIDTask.getResult().getDocuments().size() > 0) {
                                                                                DocumentSnapshot documentUserSnapshot2 = getIDTask.getResult().getDocuments().get(0);
                                                                                String docID = documentUserSnapshot2.getId();
                                                                                db.collection("billing")
                                                                                        .document(docID)
                                                                                        .update(updateBilling)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                Toast.makeText(getApplicationContext(), "bill updated", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        });
                                                scanButton.setClickable(true);
                                                submitButton.setClickable(true);
                                                txtInputPresentReading.setEnabled(true);
                                                getPrevReading();
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        db.collection("consumers")
                                .whereEqualTo("consId", consId)
                                .get()
                                .addOnCompleteListener(task3 ->{
                                    if (task3.isSuccessful() && task3.getResult() != null && task3.getResult().getDocuments().size() > 0) {
                                        DocumentSnapshot documentUserSnapshot1 = task3.getResult().getDocuments().get(0);
                                        txtPreviousReading.setText(documentUserSnapshot1.getString("firstReading"));
                                    }
                                });
                    }
                });
    }

    public int billingID() {
        Random userRandom = new Random();
        return ((1 + userRandom.nextInt(9)) * 10000 + userRandom.nextInt(10000));
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
        billingNumber = Integer.toString(year)+ "" +Integer.toString(month);
        Date now = new Date();

        billAmount = Integer.parseInt(txtWaterConsumption.getText().toString()) * watercharge;
        readingDate = readingDateFormat.format(now);
        filterDate = filterDateFormat.format(now);
        tax = billAmount * 0.12;
        status = "Pending";
        dueDate = sdf.format(getDueDate(15));
        messageDate = notifyFormat.format(getDueDate(15));
        meterImage = BitMapToString(bitmap);
        startBillingPeriod = sdf.format(getDueDate(-30));
        endBillingPeriod = sdf.format(getDueDate(-1));
        credit = 0.00; //excessPaid
        penalty = 0.00; //penalty
        discount = 0.00; // discount
        previousBalance = 0.00 - 0.00; // billAmount - paidAmount
        reconnectionFee = 0.00; // reconnectionFee
        others = 0; // others
        billAmountInvoice = 0 + 0 + 0;// billAmountInvoice + reconnectionFee + others;
        netAmount = (billAmount + previousBalance + penalty + reconnectionFee + others) - (discount+credit);
        int temp = 1;
        String finalBillingNumber= billingNumber + temp;
            //add to billing table
            db.collection("consumers")
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshotRead = task.getResult().getDocuments().get(0);
                            String firstReading = documentSnapshotRead.getString("firstReading");
                            Map<String, Object> createBilling = new HashMap<>();
                            createBilling.put("billingId", billingID);
                            createBilling.put("consId", consId);
                            createBilling.put("invoiceId", "null");
                            createBilling.put("bill_no", Integer.parseInt(finalBillingNumber));
                            createBilling.put("readingDate", readingDate);
                            createBilling.put("filterDate", filterDate);
                            createBilling.put("presentReading", txtInputPresentReading.getText().toString());
                            createBilling.put("previousReading", firstReading);
                            createBilling.put("ConsumptionUnit", txtWaterConsumption.getText().toString());
                            createBilling.put("waterCharge", (String.format("%.2f", watercharge)));
                            createBilling.put("tax", String.format("%.2f", tax));
                            createBilling.put("billAmount", String.format("%.2f", billAmount));
                            createBilling.put("others", String.format("%.2f", others));
                            createBilling.put("previousBalance", String.format("%.2f", previousBalance));
                            createBilling.put("reconnectionFee", String.format("%.2f", reconnectionFee));
                            createBilling.put("penalty", String.format("%.2f", penalty));
                            createBilling.put("discount", String.format("%.2f", discount));
                            createBilling.put("credit", String.format("%.2f", credit));
                            createBilling.put("netAmount", String.format("%.2f", netAmount));
                            createBilling.put("dueDate", dueDate);
                            createBilling.put("meterImage", meterImage);
                            createBilling.put("status", status);
                            createBilling.put("MeterReader", userDetails.getUserID());
                            createBilling.put("remarks", inputRemarks.getText().toString());
                            db.collection("billing")
                                    .whereEqualTo("consId", consId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                int counter=00001;
                                                for(DocumentSnapshot ds : task.getResult()){
                                                    counter = counter + 1;
                                                }
                                                String stringTemp = billingNumber + String.valueOf(counter);
                                                Map<String, Object> createBilling = new HashMap<>();
                                                createBilling.put("billingId", String.valueOf(billingID));
                                                createBilling.put("consId", consId);
                                                createBilling.put("invoiceId", "null");
                                                createBilling.put("bill_no", Integer.parseInt(stringTemp));
                                                createBilling.put("readingDate", readingDate);
                                                createBilling.put("filterDate", filterDate);
                                                createBilling.put("presentReading", txtInputPresentReading.getText().toString());
                                                createBilling.put("previousReading", txtPreviousReading.getText().toString());
                                                createBilling.put("ConsumptionUnit", txtWaterConsumption.getText().toString());
                                                createBilling.put("waterCharge", String.format("%.2f", watercharge));
                                                createBilling.put("tax", String.format("%.2f", tax));
                                                createBilling.put("billAmount", String.format("%.2f", billAmount));
                                                createBilling.put("others", String.format("%.2f", others));
                                                createBilling.put("previousBalance", String.format("%.2f", previousBalance));
                                                createBilling.put("reconnectionFee", String.format("%.2f", reconnectionFee));
                                                createBilling.put("penalty", String.format("%.2f", penalty));
                                                createBilling.put("discount", String.format("%.2f", discount));
                                                createBilling.put("credit", String.format("%.2f", credit));
                                                createBilling.put("netAmount", String.format("%.2f", netAmount));
                                                createBilling.put("dueDate", dueDate);
                                                createBilling.put("meterImage", meterImage);
                                                createBilling.put("status", status);
                                                createBilling.put("MeterReader", userDetails.getUserID());
                                                createBilling.put("remarks", inputRemarks.getText().toString());
                                                db.collection("billing")
                                                        .add(createBilling)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
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
                                                                                                    onBackPressed();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
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
                                    });
                        }

                    }
                });
    }
    public void notifySMS(){
        //Send to SMS
        if(ContextCompat.checkSelfPermission(ReaderProfileDetails.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){
            sendSMS();

        }else{
            ActivityCompat.requestPermissions(ReaderProfileDetails.this, new String[]{
                    Manifest.permission.SEND_SMS}, 100);
        }
    }
    public void notifyEmail(){
        //Send to email
        final String username = "crackadood095@gmail.com";
        final String password = "cqnbyusawaqmjoui";
        String messageToSend = "Dear "+name+", \n\n" +
                "We hope this letter finds you well. We are writing to notify you about your recent water bill amounting "+String.format("%.2f", netAmount)+" pesos." +
                " As a responsible consumer, it is important to keep track of your bills to ensure timely payments and to avoid any potential " +
                "service disruptions and penalties.\n\nPlease note that your current water bill is due by "+messageDate+". " +
                "If you have already paid the bill, please disregard this letter. However, if you have any questions or concerns about your bill, please do not hesitate to contact us." +
                "And our mobile application allows you to view and track your partial previous and present bills.\n\n" +
                "Additionally, we would like to use this opportunity to remind you that we only accept in-person payments at our office.\n\n" +
                "Thank you for your attention to this matter, and please let us know if you have any questions or concerns.\n\n" +
                "Best regards,\nAquatech";

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
            message.setSubject("Aquatech Water Bill Payment Due");
            message.setText(messageToSend);
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void notifyBill(){
        db.collection("consumers")
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            if(documentUserSnapshot.getString("notifyEmail").equals("1") && documentUserSnapshot.getString("notifySMS").equals("1")){
                                notifySMS();
                                notifyEmail();
                            }else if(documentUserSnapshot.getString("notifyEmail").equals("1")){
                                notifyEmail();
                            }else if(documentUserSnapshot.getString("notifySMS").equals("1")){
                                notifySMS();
                            }
                        }
                    }
                });
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
        String message = "Dear "+name+", \n\n" +
                "We hope this letter finds you well. We are writing to notify you about your recent water bill amounting "+String.format("%.2f", netAmount)+" pesos." +
                " As a responsible consumer, it is important to keep track of your bills to ensure timely payments and to avoid any potential " +
                "service disruptions and penalties.\n\nPlease note that your current water bill is due by "+messageDate+". " +
                " If you have already paid the bill, please disregard this letter. However, if you have any questions or concerns about your bill, please do not hesitate to contact us." +
                " And our mobile application allows you to view and track your partial previous and present bills.\n\n" +
                " Additionally, we would like to use this opportunity to remind you that we only accept in-person payments at our office.\n\n" +
                " Thank you for your attention to this matter, and please let us know if you have any questions or concerns.\n\n" +
                "Best regards,\nAquatech";

        if(!phone.isEmpty() && !message.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
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
}