package com.example.chatapp.utilities;

public class UserDetailsRecyclerView {

    String name;
    String meterSerialNumber;
    String image;

    public UserDetailsRecyclerView(){}
    public UserDetailsRecyclerView(String name, String meterSerialNumber){
        this.name = name;
        this.meterSerialNumber = meterSerialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }





}
