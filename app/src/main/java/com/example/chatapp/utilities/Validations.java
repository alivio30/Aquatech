package com.example.chatapp.utilities;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class Validations {

    public boolean isValidEmail(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public String invalidEmailFormat(){
        return "Email format is incorrect.";
    }
    public String emptyFields(){
        return "Please input necessary field/s.";
    }

    public String passwordNotMatched(){
        return "Password not matched.";
    }
    public String nullImage(){
        return "Please select an image.";
    }
    public String selectBillMethod(){
        return "Please select preferred bill notification";
    }
    public String selectConsumerType(){
        return "Please select consumer type";
    }
    public String existingEmail(){
        return "Email already taken.";
    }
    public String existingSerialNumber(){
        return "Serial number already taken";
    }
    public String registerSuccess(){
        return "Registered successfully.";
    }
    public String registerFail(){
        return "Failed to register.";
    }
    public String contactNumberLimit(){
        return "Invalid contact number format";
    }
}
