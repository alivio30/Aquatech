package com.example.chatapp.FragmentEmpPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.CreateUser;
import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminBillingDetails extends Fragment {
    View view;
    UserDetails userDetails = new UserDetails();
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinnerYear, spinnerMonth;
    String getYear, getMonth, filter;
    String startBillingPeriod, endBillingPeriod;
    String readingDate, dateCreated, endReading, autoDate;
    LinearLayout layout;
    TextView textNoBill;
    ImageView imageWaterMeter, imageBack;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat readingFormat = new SimpleDateFormat("MMMM-yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    String consId ="";
    String month, year;
    TextView period, totalBill, billingNumber, presentReading, previousReading, waterConsumed, billAmount, penalty, reconnectionFee, serialNumber, meterReader, dueDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_billing_details, container, false);
        billingNumber = view.findViewById(R.id.textBillNo);
        presentReading = view.findViewById(R.id.textPresentReading);
        previousReading = view.findViewById(R.id.textPreviousReading);
        waterConsumed = view.findViewById(R.id.textWaterConsumed);
        billAmount = view.findViewById(R.id.textBillAmount);
        penalty = view.findViewById(R.id.textPenalty);
        reconnectionFee = view.findViewById(R.id.textReconnectionFee);
        serialNumber = view.findViewById(R.id.textSerialNo);
        meterReader = view.findViewById(R.id.textMeterReader);
        dueDate = view.findViewById(R.id.textDueDate);
        totalBill = view.findViewById(R.id.textTotalBill);
        period = view.findViewById(R.id.textBillingPeriod);
        imageWaterMeter = view.findViewById(R.id.imageWaterMeter);
        imageBack = view.findViewById(R.id.imageBack);
        layout = view.findViewById(R.id.LinearContainer);
        textNoBill = view.findViewById(R.id.textNoBill);
        if(userDetails.getUserType().equalsIgnoreCase("consumer")){
            imageBack.setVisibility(View.GONE);
        }
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MasterPage.class);
                startActivity(intent);
            }
        });
        //spinners
        spinnerYear = view.findViewById(R.id.spinnerYear);
        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        //spinner year
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerYear, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerMonth, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        setData(spinnerMonth, spinnerYear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getYear = adapterView.getItemAtPosition(i).toString();
                displayBillingDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getMonth = adapterView.getItemAtPosition(i).toString();
                displayBillingDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
    public void setData(Spinner spinnerM, Spinner spinnerY){
        if(userDetails.getUserType().equalsIgnoreCase("admin")){
            String userID = getArguments().getString("userId");
            db.collection("consumers")
                    .whereEqualTo("userId", userID)
                    .get()
                    .addOnCompleteListener(consumerTask -> {
                        if (consumerTask.isSuccessful() && consumerTask.getResult() != null && consumerTask.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentConsumerSnapshot = consumerTask.getResult().getDocuments().get(0);
                            userDetails.setConsumerID(documentConsumerSnapshot.getString("consId"));
                            userDetails.setSerialNumber(documentConsumerSnapshot.getString("meterSerialNumber"));
                            userDetails.setTankNumber(documentConsumerSnapshot.getString("tankNumber"));
                            userDetails.setPumpNumber(documentConsumerSnapshot.getString("pumpNumber"));
                            userDetails.setLineNumber(documentConsumerSnapshot.getString("lineNumber"));
                            userDetails.setMeterStandNumber(documentConsumerSnapshot.getString("meterStandNumber"));
                            userDetails.setConsumerType(documentConsumerSnapshot.getString("consumerType"));
                            displayLatestBill(userDetails.getConsumerID(), spinnerM, spinnerY);
                        }
                    });
        }
        if (userDetails.getUserType().equalsIgnoreCase("consumer")) {
            db.collection("consumers")
                    .whereEqualTo("userId", userDetails.getUserID())
                    .get()
                    .addOnCompleteListener(consumerTask -> {
                        if (consumerTask.isSuccessful() && consumerTask.getResult() != null && consumerTask.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentConsumerSnapshot = consumerTask.getResult().getDocuments().get(0);
                            userDetails.setConsumerID(documentConsumerSnapshot.getString("consId"));
                            userDetails.setSerialNumber(documentConsumerSnapshot.getString("meterSerialNumber"));
                            userDetails.setTankNumber(documentConsumerSnapshot.getString("tankNumber"));
                            userDetails.setPumpNumber(documentConsumerSnapshot.getString("pumpNumber"));
                            userDetails.setLineNumber(documentConsumerSnapshot.getString("lineNumber"));
                            userDetails.setMeterStandNumber(documentConsumerSnapshot.getString("meterStandNumber"));
                            userDetails.setConsumerType(documentConsumerSnapshot.getString("consumerType"));
                            displayLatestBill(userDetails.getConsumerID(), spinnerM, spinnerY);
                        }
                    });
        }
    }
    public void displayLatestBill(String consId, Spinner spinnerM, Spinner spinnerY){
        db.collection("billing").whereEqualTo("consId", consId)
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
                        db.collection("billing").whereEqualTo("bill_no", value)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if (task1.isSuccessful() && task1.getResult() != null && task1.getResult().getDocuments().size() > 0) {
                                            DocumentSnapshot documentUserSnapshot = task1.getResult().getDocuments().get(0);
                                            autoDate = documentUserSnapshot.getString("filterDate");
                                            try {
                                                month = monthFormat.format(getMonth());
                                                year = yearFormat.format(getYear());
                                                String m = month;
                                                spinnerM.setSelection(getMonthIndex(spinnerM, m));
                                                String y= year;
                                                spinnerY.setSelection(getYearIndex(spinnerY, y));

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                    }
                });
    }
    private int getMonthIndex(Spinner spinnerM, String m) {
        for(int i=0; i<spinnerM.getCount(); i++){
            if(spinnerM.getItemAtPosition(i).toString().equalsIgnoreCase(m)){
                return i;
            }
        }
        return 0;
    }
    private int getYearIndex(Spinner spinnerY, String y) {
        for(int i=0; i<spinnerY.getCount(); i++){
            if(spinnerY.getItemAtPosition(i).toString().equalsIgnoreCase(y)){
                return i;
            }
        }
        return 0;
    }
    public void displayBillingDetails(){
        filter = getMonth+"-"+getYear;
        billingNumber.setText("");
        presentReading.setText("");
        previousReading.setText("");
        waterConsumed.setText("");
        billAmount.setText("");
        penalty.setText("");
        reconnectionFee.setText("");
        meterReader.setText("");
        dueDate.setText("");
        serialNumber.setText("");
        imageWaterMeter.setImageDrawable(null);
        layout.setVisibility(View.GONE);
        textNoBill.setVisibility(View.VISIBLE);
        if(userDetails.getUserType().equalsIgnoreCase("admin")) consId = consumerProfileDetails.getConsID();
        else if(userDetails.getUserType().equalsIgnoreCase("consumer")) consId = userDetails.getConsumerID();
        db.collection("billing")
                .whereEqualTo("filterDate", filter)
                .whereEqualTo("consId", consId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot1 = task.getResult().getDocuments().get(0);
                            String bill_no = documentSnapshot1.getLong("bill_no").toString();
                            int bill_no_int = Integer.parseInt(bill_no);
                            layout.setVisibility(View.VISIBLE);
                            textNoBill.setVisibility(View.GONE);
                            billingNumber.setText(documentSnapshot1.getLong("bill_no").toString());
                            presentReading.setText(documentSnapshot1.getString("presentReading"));
                            previousReading.setText(documentSnapshot1.getString("previousReading"));
                            waterConsumed.setText(documentSnapshot1.getString("ConsumptionUnit"));
                            billAmount.setText(documentSnapshot1.getString("billAmount"));
                            penalty.setText(documentSnapshot1.getString("penalty"));
                            reconnectionFee.setText(documentSnapshot1.getString("reconnectionFee"));
                            meterReader.setText(documentSnapshot1.getString("MeterReader"));
                            dueDate.setText(documentSnapshot1.getString("dueDate"));
                            totalBill.setText(documentSnapshot1.getString("netAmount"));
                            StringToBitMap(imageWaterMeter, documentSnapshot1.getString("meterImage"));
                            db.collection("consumers").whereEqualTo("consId", consId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                            DocumentSnapshot documentSnapshot2 = task.getResult().getDocuments().get(0);
                                            String userId = documentSnapshot2.getString("userId");
                                            serialNumber.setText(documentSnapshot2.getString("meterSerialNumber"));
                                            db.collection("users").whereEqualTo("userId", userId)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                DocumentSnapshot documentSnapshot3 = task.getResult().getDocuments().get(0);
                                                                dateCreated = documentSnapshot3.getString("Date Created");
                                                                readingDate = documentSnapshot1.getString("readingDate");
                                                                try {
                                                                    startBillingPeriod = format.format(getDateCreated(1));
                                                                    endBillingPeriod = format.format(getReadingDate(-1));
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                period.setText(startBillingPeriod+" -- "+endBillingPeriod);
                                                                db.collection("billing").whereEqualTo("consId", consId).whereEqualTo("bill_no", bill_no_int-1)
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                                    DocumentSnapshot documentSnapshot2 = task.getResult().getDocuments().get(0);
                                                                                    readingDate = documentSnapshot2.getString("readingDate");
                                                                                    endReading = documentSnapshot1.getString("readingDate");
                                                                                    try {
                                                                                        startBillingPeriod = format.format(getReadingDate(1));
                                                                                        endBillingPeriod = format.format(getEndReading(-1));
                                                                                    } catch (ParseException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    period.setText(startBillingPeriod+" -- "+endBillingPeriod);
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                        }
                    }
                });
    }
    public void StringToBitMap(ImageView bitImage, String image){
        byte[] imageBytes;
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        bitImage.setImageBitmap(bitmap);
    }
    public Date getReadingDate(int date) throws ParseException {
        Date d = format.parse(this.readingDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, date);

        return cal.getTime();
    }
    public Date getDateCreated(int date) throws ParseException {
        Date d = format.parse(this.dateCreated);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, date);

        return cal.getTime();
    }
    public Date getEndReading(int date) throws ParseException {
        Date d = format.parse(this.endReading);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, date);

        return cal.getTime();
    }
    public Date getMonth() throws ParseException {
        Date d = readingFormat.parse(this.autoDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        return cal.getTime();
    }
    public Date getYear() throws ParseException {
        Date d = readingFormat.parse(this.autoDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        return cal.getTime();
    }
}