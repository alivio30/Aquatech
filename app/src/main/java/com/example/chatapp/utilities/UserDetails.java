package com.example.chatapp.utilities;

public class UserDetails {
    private static String name;
    private static String username;
    private static String password;
    private static String email;
    private static String address;
    private static String contactNumber;
    private static String image;
    private static String userType;
    private static String userID;
    private static String companyID;
    private static String companyName;

    //for consumer
    private static String consumerID;
    private static String serialNumber;
    private static String tankNumber;
    private static String pumpNumber;
    private static String lineNumber;
    private static String meterStandNumber;
    private static String consumerType;


    //set values
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    public void setCompanyID(String companyID){
        this.companyID = companyID;
    }
    public void setImage(String image){
        this.image = image;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
    }
    public void setUserType(String userType){
        this.userType = userType;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }
    public void setConsumerID(String consumerID){
        this.consumerID = consumerID;
    }
    public void setSerialNumber(String serialNumber){
        this.serialNumber = serialNumber;
    }
    public void setTankNumber(String tankNumber){
        this.tankNumber = tankNumber;
    }
    public void setPumpNumber(String pumpNumber){
        this.pumpNumber = pumpNumber;
    }
    public void setLineNumber(String lineNumber){
        this.lineNumber = lineNumber;
    }
    public void setMeterStandNumber(String meterStandNumber){
        this.meterStandNumber = meterStandNumber;
    }
    public void setConsumerType(String consumerType){
        this.consumerType = consumerType;
    }

    //get values
    public String getCompanyName(){
        return companyName;
    }
    public String getCompanyID(){
        return companyID;
    }
    public String getImage(){
        return image;
    }
    public String getName(){
        return name;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getAddress(){
        return address;
    }
    public String getContactNumber(){
        return contactNumber;
    }
    public String getUserType(){
        return userType;
    }
    public String getUserID(){
        return userID;
    }
    public String getConsumerID(){
        return consumerID;
    }
    public String getSerialNumber(){
        return serialNumber;
    }
    public String getTankNumber(){
        return tankNumber;
    }
    public String getPumpNumber(){
        return pumpNumber;
    }
    public String getLineNumber(){
        return lineNumber;
    }
    public String getMeterStandNumber(){
        return meterStandNumber;
    }
    public String getConsumerType(){
        return consumerType;
    }
}
