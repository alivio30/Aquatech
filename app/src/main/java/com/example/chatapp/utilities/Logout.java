package com.example.chatapp.utilities;

public class Logout {
    UserDetails userDetails = new UserDetails();
    PreferenceManager preferenceManager;
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    UserDetailsRecyclerView userDetailsRecyclerView = new UserDetailsRecyclerView();

    //clear data after logout
    public void clearAllData(){
        //preferenceManager.clear();
        userDetails.setName("");
        userDetails.setUsername("");
        userDetails.setPassword("");
        userDetails.setEmail("");
        userDetails.setAddress("");
        userDetails.setContactNumber("");
        userDetails.setImage("");
        userDetails.setUserType("");
        userDetails.setUserID("");
        userDetails.setConsumerID("");
        userDetails.setSerialNumber("");
        userDetails.setTankNumber("");
        userDetails.setPumpNumber("");
        userDetails.setLineNumber("");
        userDetails.setMeterStandNumber("");
        userDetails.setConsumerType("");

        consumerProfileDetails.setName("");
        consumerProfileDetails.setRemarks("");
        consumerProfileDetails.setStatus("");
        consumerProfileDetails.setEmail("");
        consumerProfileDetails.setAddress("");
        consumerProfileDetails.setContactNumber("");
        consumerProfileDetails.setDateApplied("");
        consumerProfileDetails.setAccountNumber("");
        consumerProfileDetails.setUserID("");
        consumerProfileDetails.setConsID("");
        consumerProfileDetails.setMeterSerialNumber("");
        consumerProfileDetails.setTankNumber("");
        consumerProfileDetails.setPumpNumber("");
        consumerProfileDetails.setLineNumber("");
        consumerProfileDetails.setMeterStandNumber("");
        consumerProfileDetails.setConsumerType("");

        userDetailsRecyclerView.setName("");
        userDetailsRecyclerView.setAccountNumber("");
        userDetailsRecyclerView.setConsId("");
        userDetailsRecyclerView.setConsumerType("");
        userDetailsRecyclerView.setMeterStandNumber("");
        userDetailsRecyclerView.setPumpNumber("");
        userDetailsRecyclerView.setRemarks("");
        userDetailsRecyclerView.setStatus("");
        userDetailsRecyclerView.setTankNumber("");
        userDetailsRecyclerView.setUserId("");
        userDetailsRecyclerView.setMeterSerialNumber("");
        userDetailsRecyclerView.setLineNumber("");
        userDetailsRecyclerView.setEmail("");
        userDetailsRecyclerView.setImage("");
        userDetailsRecyclerView.setAddress("");
    }
}
