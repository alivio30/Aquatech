package com.example.chatapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Pattern;

import com.example.chatapp.ActivityEmpPage.CreateUser;
import com.example.chatapp.R;
import com.example.chatapp.activities.SignInActivity;
import com.example.chatapp.utilities.Validations;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPassword extends Fragment {
    View view;
    EditText inputEmail;
    Button submit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Validations validations = new Validations();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        inputEmail = view.findViewById(R.id.inputEmail);
        submit = view.findViewById(R.id.buttonSend);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputEmail.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), validations.emptyFields(), Toast.LENGTH_SHORT).show();
                }else if(!validations.isValidEmail(inputEmail.getText().toString().trim())){
                    Toast.makeText(getContext(), validations.invalidEmailFormat(), Toast.LENGTH_SHORT).show();
                }else{
                    db.collection("users")
                            .whereEqualTo("email", inputEmail.getText().toString())
                            .get()
                            .addOnCompleteListener(task ->{
                                if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                                    DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                                    final String username = "aquatech.system2023@gmail.com";
                                    final String password = "jcohwldssphwwxjc";
                                    String messageToSend = "Dear "+ documentUserSnapshot.getString("name")+",\n\nWe received your recent request for your old password. As per your request, we have retrieved your old " +
                                            "password and it is '"+documentUserSnapshot.getString("password")+"'. Please note that for security reasons, we strongly advise that you change your password immediately after " +
                                            "logging in.\n\nWe understand that forgetting passwords can be frustrating, which is why we recommend using password managers or other secure methods to store your login information. " +
                                            "This will not only make it easier to access your accounts, but also help ensure the security of your personal information.\n\nIf you have any further questions or concerns, please do not " +
                                            "hesitate to contact us. We are here to assist you in any way we can.\n\nThank you for your continued patronage.\n\nSincerely,\nAquatech";
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
                                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(inputEmail.getText().toString()));
                                        message.setSubject("Forgot Password Assistance");
                                        message.setText(messageToSend);
                                        Transport.send(message);
                                        Toast.makeText(getContext(), "Email sent!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), SignInActivity.class);
                                        startActivity(intent);
                                    } catch (AddressException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    } catch (MessagingException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getContext(), "Incorrect email address.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(failTask ->{
                                Toast.makeText(getContext(), "Incorrect email address.", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return view;
    }
}
