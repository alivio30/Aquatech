package com.example.chatapp.ActivityEmpPage;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.activities.MainActivity2;
import com.example.chatapp.utilities.CropperActivity;
import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    Dialog dialog;
    TextView dialogAttempt, dialogTextResult, dialogText, txtPreviousReading, txtInputPresentReading, txtWaterConsumption, txtaddres, txtname, txtaccountNumber, txtserialNumber, txtpumpNumber, txttankNumber, txtlineNumber, txtmeterStandNumber;
    String consId, mail, number, name, accountNumber, meterStandNumber, pumpNumber, tankNumber, meterSerialNumber, lineNumber, image, address;
    ImageView imageProfile, scannedMeter, previousScannedMeter, imageBack, result;
    Button scanButton, submitButton, yes, no;
    Bitmap bitmap, croppedImage;
    String modifiedText="";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Toast toast;
    Calendar calendar = Calendar.getInstance();
    EditText inputRemarks;
    SimpleDateFormat notifyFormat = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat readingDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat filterDateFormat = new SimpleDateFormat("MMMM yyyy");
    int counter = 3, billingID = 1, year, month, day;
    double tax=0, watercharge=10.00, billAmountInvoice=0.00, billAmount=0.00, netAmount=100.00, credit=0.00, penalty=0.00, discount=0.00, previousBalance=0.00, reconnectionFee=0.00, others=0.00;
    String billingNumber, messageDate, messageNetAmount, meterImage, bill_no, readingDate, filterDate, status, startBillingPeriod, endBillingPeriod, dueDate;
    public static final int requestCameraCode = 12;
    boolean resultFlag = false;
    String currentPhotoPath;
    File imageFile, file;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    //camera
    ActivityResultLauncher<Intent> takePictureLauncher;
    Uri resultUri;

    //request camera
    private static final int REQUEST_CAMERA_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_profile_details);
        //for scan dialog
        showDialog();

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
        txtInputPresentReading.setEnabled(false);

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
                scannedMeter.setImageDrawable(null);
                onBackPressed();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, requestCameraCode);*/
                openCamera();
            }
        });

        if(ContextCompat.checkSelfPermission(ReaderProfileDetails.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ReaderProfileDetails.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtInputPresentReading.getText().toString().trim().isEmpty() || scannedMeter.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "Please input necessary fields", Toast.LENGTH_SHORT).show();
                }else if(txtWaterConsumption.getText().toString().isEmpty()){
                    builder.setTitle("Alert!")
                            .setMessage("Water Consumption is empty, please scan again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    counter=3;
                                    scanButton.setBackgroundResource(android.R.drawable.btn_default);
                                    scanButton.setClickable(true);
                                    txtInputPresentReading.setText("");
                                    scannedMeter.setImageDrawable(null);
                                }
                            })
                            .show();
                }else{
                    if(Integer.parseInt(txtWaterConsumption.getText().toString()) < 0){
                        builder.setTitle("Alert!")
                                .setMessage("Water Consumption went to negative! Please scan again.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        counter=3;
                                        scanButton.setBackgroundResource(android.R.drawable.btn_default);
                                        scanButton.setClickable(true);
                                        txtInputPresentReading.setText("");
                                        scannedMeter.setImageDrawable(null);
                                    }
                                })
                                .show();
                    }else{
                        calculateBill();
                    }
                }
            }
        });
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Get the captured image from the result intent
                        Bundle extras = result.getData().getExtras();
                        imageBitmap = (Bitmap) extras.get("data");


                        FileOutputStream fos = null;
                        try {
                            // create a temporary file to save the compressed bitmap
                            file = File.createTempFile("compressed_", ".jpg", getCacheDir());
                            fos = new FileOutputStream(file);
                            // write the compressed bitmap to the file
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri compressedUri = Uri.fromFile(file);

                        // Pass the image to the cropper activity
                        Intent intent = new Intent(ReaderProfileDetails.this, CropperActivity.class);
                        intent.putExtra("DATA", compressedUri+"");
                        startActivityForResult(intent, 101);
                    }
                });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        result();
    }

    public void showDialog(){
        dialog = new Dialog(ReaderProfileDetails.this);
        dialog.setContentView(R.layout.scan_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        yes = dialog.findViewById(R.id.btn_yes);
        no = dialog.findViewById(R.id.btn_no);
        result = dialog.findViewById(R.id.imageResult);
        dialogText = dialog.findViewById(R.id.textPhrase);
        dialogTextResult = dialog.findViewById(R.id.textResult);
        dialogAttempt = dialog.findViewById(R.id.textAttempt);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                resultFlag = true;
                if(resultFlag){
                    txtInputPresentReading.setText(modifiedText);
                    scannedMeter.setImageBitmap(croppedImage);
                    result();
                    txtInputPresentReading.setEnabled(false);
                    scanButton.setClickable(false);
                    scanButton.setBackgroundColor(Color.rgb(255, 0, 0));
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                if(counter!=0){
                    openCamera();
                }else{
                    dialog.dismiss();
                    builder.setTitle("Manual input override..")
                            .setMessage("Please input the present reading manually.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    scanButton.setClickable(false);
                                    scanButton.setBackgroundColor(Color.rgb(255, 0, 0));
                                }
                            })
                            .show();
                    txtInputPresentReading.setEnabled(true);
                    txtInputPresentReading.requestFocus();
                    scannedMeter.setImageBitmap(croppedImage);
                }
                dialog.dismiss();
            }
        });
    }
    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Launch the camera to capture an image
            takePictureLauncher.launch(takePictureIntent);
        }
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
                        long value = (long)maxValue;
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
                        long value = (long)maxValue;
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
                                            if(dueDate.after(now)){     //before duedate
                                                scanButton.setClickable(false);
                                                submitButton.setClickable(false);
                                                txtInputPresentReading.setEnabled(false);
                                                scanButton.setBackgroundColor(Color.rgb(255, 0, 0));
                                                submitButton.setBackgroundColor(Color.rgb(255, 0, 0));

                                                txtInputPresentReading.setText(documentUserSnapshot.getString("presentReading"));
                                                StringToBitMap(scannedMeter, documentUserSnapshot.getString("meterImage"));
                                                txtWaterConsumption.setText(documentUserSnapshot.getString("ConsumptionUnit"));
                                                inputRemarks.setText(documentUserSnapshot.getString("remarks"));
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
                                                //after 15 days of reading date, the button will be enabled
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
                                                                                                Map<String, Object> updateStatus = new HashMap<>();
                                                                                                updateStatus.put("remarks", "Unread");
                                                                                                db.collection("consumers")
                                                                                                        .whereEqualTo("consId", consId)
                                                                                                        .get()
                                                                                                        .addOnCompleteListener(updateTask1 -> {
                                                                                                            if (updateTask1.isSuccessful() && updateTask1.getResult() != null && updateTask1.getResult().getDocuments().size() > 0) {
                                                                                                                DocumentSnapshot documentUserSnapshot3 = updateTask1.getResult().getDocuments().get(0);
                                                                                                                String docId = documentUserSnapshot3.getId();
                                                                                                                db.collection("consumers")
                                                                                                                        .document(docId)
                                                                                                                        .update(updateStatus);
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        });
                                                scanButton.setClickable(true);
                                                submitButton.setClickable(true);
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
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setVisibility((View.INVISIBLE));
        billingID = billingID();
        messageNetAmount = String.format("%.2f", netAmount);
        messageDate = notifyFormat.format(getDueDate(15));
        billingNumber = consId +"" +Integer.toString(year)+ "" +Integer.toString(month);
        Date now = new Date();
        db.collection("consumers").whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        db.collection("rate").whereEqualTo("companyId", documentUserSnapshot.getString("companyId")).whereEqualTo("rateDescription", documentUserSnapshot.getString("consumerType"))
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult() != null && task1.getResult().getDocuments().size() > 0) {
                                        int consumption = Integer.parseInt(txtWaterConsumption.getText().toString());
                                        for(DocumentSnapshot ds : task1.getResult().getDocuments()){
                                            //int consumption1 = 0;
                                            if(consumption >= Integer.parseInt(ds.getString("rateStartUnit")) && consumption <= Integer.parseInt(ds.getString("rateEndUnit"))) {
                                                watercharge = Integer.parseInt((ds.getString("ratePrice")));
                                            }
                                        }
                                        billAmount = Integer.parseInt(txtWaterConsumption.getText().toString()) * watercharge;
                                        readingDate = readingDateFormat.format(now);
                                        filterDate = filterDateFormat.format(now);
                                        tax = billAmount * 0.12;
                                        status = "Pending";
                                        dueDate = sdf.format(getDueDate(15));
                                        messageDate = notifyFormat.format(getDueDate(15));
                                        meterImage = BitMapToString(croppedImage);
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
                                        String finalBillingNumber = billingNumber + temp;
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
                                                            createBilling.put("billingId", String.valueOf(billingID));
                                                            createBilling.put("consId", consId);
                                                            createBilling.put("invoiceId", "null");
                                                            createBilling.put("bill_no", Long.parseLong(finalBillingNumber));
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
                                                            createBilling.put("companyId", userDetails.getCompanyID());
                                                            db.collection("billing")
                                                                    .whereEqualTo("consId", consId)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                                                                                int counter=00001;
                                                                                for(DocumentSnapshot ds : task.getResult()){
                                                                                    counter = counter + 1;
                                                                                }
                                                                                String stringTemp = billingNumber + String.valueOf(counter);
                                                                                db.collection("billing").whereEqualTo("consId", consId)
                                                                                        .orderBy("bill_no", Query.Direction.DESCENDING).limit(1)
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                                                                                                    DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                                                                                                    String others1, previousBalance1, reconnectionFee1, penalty1, discount1, credit1;
                                                                                                    double netAmount1;
                                                                                                    others1 = documentUserSnapshot.getString("others");
                                                                                                    previousBalance1 = documentUserSnapshot.getString("previousBalance");
                                                                                                    reconnectionFee1 = documentUserSnapshot.getString("reconnectionFee");
                                                                                                    penalty1 = documentUserSnapshot.getString("penalty");
                                                                                                    discount1 = documentUserSnapshot.getString("discount");
                                                                                                    credit1 = documentUserSnapshot.getString("credit");
                                                                                                    netAmount1 = (billAmount + Double.parseDouble(previousBalance1) + penalty + reconnectionFee + others) - (discount + Double.parseDouble(credit1));

                                                                                                    Map<String, Object> createBilling = new HashMap<>();
                                                                                                    createBilling.put("billingId", String.valueOf(billingID));
                                                                                                    createBilling.put("consId", consId);
                                                                                                    createBilling.put("invoiceId", "null");
                                                                                                    createBilling.put("bill_no", Long.parseLong(stringTemp));
                                                                                                    createBilling.put("readingDate", readingDate);
                                                                                                    createBilling.put("filterDate", filterDate);
                                                                                                    createBilling.put("presentReading", txtInputPresentReading.getText().toString());
                                                                                                    createBilling.put("previousReading", txtPreviousReading.getText().toString());
                                                                                                    createBilling.put("ConsumptionUnit", txtWaterConsumption.getText().toString());
                                                                                                    createBilling.put("waterCharge", String.format("%.2f", watercharge));
                                                                                                    createBilling.put("tax", String.format("%.2f", tax));
                                                                                                    createBilling.put("billAmount", String.format("%.2f", billAmount));
                                                                                                    createBilling.put("others", String.format("%.2f", others));
                                                                                                    createBilling.put("previousBalance", String.format("%.2f", Double.parseDouble(previousBalance1)));
                                                                                                    createBilling.put("reconnectionFee", String.format("%.2f", reconnectionFee));
                                                                                                    createBilling.put("penalty", String.format("%.2f", penalty));
                                                                                                    createBilling.put("discount", String.format("%.2f", discount));
                                                                                                    createBilling.put("credit", String.format("%.2f", credit));
                                                                                                    createBilling.put("netAmount", String.format("%.2f", netAmount1));
                                                                                                    createBilling.put("dueDate", dueDate);
                                                                                                    createBilling.put("meterImage", meterImage);
                                                                                                    createBilling.put("status", status);
                                                                                                    createBilling.put("MeterReader", userDetails.getUserID());
                                                                                                    createBilling.put("remarks", inputRemarks.getText().toString());
                                                                                                    createBilling.put("companyId", userDetails.getCompanyID());
                                                                                                    db.collection("billing").document(name+" - "+stringTemp+" - "+userDetails.getCompanyName())
                                                                                                            .set(createBilling)
                                                                                                            .addOnSuccessListener(documentReference -> {
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
                                                                                                                                            .update("remarks", "Read")
                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                                    notifyBill();
                                                                                                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                                                                                                    submitButton.setVisibility((View.VISIBLE));
                                                                                                                                                    onBackPressed();
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                            });
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }else{
                                                                                db.collection("billing").document(name+" - "+finalBillingNumber+" - "+userDetails.getCompanyName())
                                                                                        .set(createBilling)
                                                                                        .addOnSuccessListener(documentReference -> {
                                                                                            /*toast = Toast.makeText(getApplicationContext(), "Saved to billing!", Toast.LENGTH_SHORT);
                                                                                            toast.show();*/
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
                                                                                                                        .update("remarks", "Read")
                                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                notifyBill();
                                                                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                                                                                submitButton.setVisibility((View.VISIBLE));
                                                                                                                                onBackPressed();
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        })
                                                                                        .addOnFailureListener(exception -> {
                                                                                            toast = Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT);
                                                                                            toast.show();
                                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                                            submitButton.setVisibility((View.VISIBLE));
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                        }

                                                    }
                                                });
                                    }
                                });
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
        final String username = "aquatech.system2023@gmail.com";
        final String password = "jcohwldssphwwxjc";
        String messageToSend = "Dear "+name+", \n\n" +
                "We hope this letter finds you well. We are writing to notify you about your recent water bill amounting "+String.format("%.2f", netAmount)+" pesos for the month of "+filterDate+"." +
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
                            /**if(documentUserSnapshot.getString("notifyEmail").equals("1") && documentUserSnapshot.getString("notifySMS").equals("1")){
                                notifySMS();
                                notifyEmail();*/
                            if(documentUserSnapshot.getString("notifyEmail").equals("1")){
                                notifyEmail();
                            }
                            if(documentUserSnapshot.getString("notifySMS").equals("1")){
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
        messageNetAmount = String.format("%.2f", netAmount);
        SmsManager smsManager = SmsManager.getDefault();
        String phone = number;
        String message = "Dear "+name+", \n\n" +
                "Your partial bill is amounting "+messageNetAmount+" pesos for the month of "+filterDate+"." +
                "\nDue date is by "+messageDate+". " +
                "\n\nBest regards,\nAquatech";

        try {
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent successfully.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed to send.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
    //----------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1 && requestCode==101)
        {
            String result = data.getStringExtra("RESULT");
            resultUri = null;
            if(result!=null)
                try {
                    resultUri=Uri.parse(result);
                    croppedImage = getBitmapFromUri(resultUri);
                    //scannedMeter.setImageBitmap(croppedImage);
                    //getTextFromImage(bitmap);
                    runTextRecognition();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
        input.close();
        return bitmap;
    }

    private void runTextRecognition() throws IOException {
        InputImage image = InputImage.fromBitmap(getBitmapFromUri(resultUri), 0);
        TextRecognizer recognizer = TextRecognition.getClient();

        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        processTextRecognitionResult(visionText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
    private void processTextRecognitionResult(Text texts) {
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            counter--;
            showToast("No text found");
            builder.setTitle("Alert!")
                    .setMessage("No text found!\n\n"+"Number of attempt/s: "+counter)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dialogInterface.cancel();\
                            if(counter!=0){
                                openCamera();
                            }else if(counter == 0){
                                dialogInterface.cancel();
                                    builder.setTitle("Manual input override..")
                                            .setMessage("Please input the present reading manually.")
                                            .setCancelable(true)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    scanButton.setClickable(false);
                                                    scanButton.setBackgroundColor(Color.rgb(255, 0, 0));
                                                }
                                            })
                                            .show();
                                        txtInputPresentReading.setEnabled(true);
                                        txtInputPresentReading.requestFocus();
                                        scannedMeter.setImageBitmap(croppedImage);
                                    }
                            }
                    })
                    .show();
            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            List<Text.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    String elementText = elements.get(k).getText();
                    showToast(elementText);
                    modifiedText = elementText.replace("O", "0").replace("o", "0").replace("B", "8").replace("D", "0").replace("d", "0");
                    modifiedText = modifiedText.replaceAll("[^a-zA-Z0-9]", "");
                }
            }
        }
        dialog.show();
        result.setImageBitmap(croppedImage);
        dialogTextResult.setText(modifiedText);
        dialogText.setText("Is this the correct result?");
        dialogAttempt.setText("Number of attempt/s: "+counter);
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //calculates the result real-time
    public void result(){
        txtInputPresentReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    try{
                        int number = Integer.parseInt(s.toString());
                        int result = number - Integer.parseInt(txtPreviousReading.getText().toString()); // Perform your calculation here
                        txtWaterConsumption.setText(String.valueOf(result));
                    }catch(NumberFormatException e){
                        txtWaterConsumption.setText("");
                    }
                } else {
                    txtWaterConsumption.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}