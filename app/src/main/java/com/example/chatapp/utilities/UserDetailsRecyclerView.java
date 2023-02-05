package com.example.chatapp.utilities;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.consumerPage.CMasterPage;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailsRecyclerView {
    String name;
    String accountNumber;
    String consId;
    String consumerType;
    String meterStandNumber;
    String pumpNumber;
    String remarks;
    String status;
    String tankNumber;
    String userId;
    String meterSerialNumber;
    String lineNumber;
    String email;
    String image;
    String address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getMeterStandNumber() {
        return meterStandNumber;
    }

    public void setMeterStandNumber(String meterStandNumber) {
        this.meterStandNumber = meterStandNumber;
    }

    public String getPumpNumber() {
        return pumpNumber;
    }

    public void setPumpNumber(String pumpNumber) {
        this.pumpNumber = pumpNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTankNumber() {
        return tankNumber;
    }

    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }
    public UserDetailsRecyclerView(){}
    /**public UserDetailsRecyclerView(String name, String meterSerialNumber, String lineNumber){
        this.name = name;
        this.meterSerialNumber = meterSerialNumber;
        this.lineNumber = lineNumber;
    }*/





}
