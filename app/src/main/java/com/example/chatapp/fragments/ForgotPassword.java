package com.example.chatapp.fragments;

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

import com.example.chatapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPassword extends Fragment {
    View view;
    EditText inputEmail;
    Button submit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                db.collection("users")
                    .whereEqualTo("email", inputEmail.getText().toString())
                    .get()
                    .addOnCompleteListener(task ->{
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                            final String username = "crackadood095@gmail.com";
                            final String password = "cqnbyusawaqmjoui";
                            String messageToSend = "Hello, "+ documentUserSnapshot.getString("name")+"! Your password is '"+documentUserSnapshot.getString("password");
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
                                message.setSubject("Sending message to receiver through app");
                                message.setText(messageToSend);
                                Transport.send(message);
                                Toast.makeText(getContext(), "email send successfull", Toast.LENGTH_SHORT).show();
                            } catch (AddressException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(failTask ->{
                            Toast.makeText(getContext(), "Email address not found!", Toast.LENGTH_SHORT).show();
                        });
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return view;
    }

    /**public void sendEmail(){
        final String username = "crackadood095@gmail.com";
        final String password = "cqnbyusawaqmjoui";
        String messageToSend = "testing";
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
            message.setSubject("Sending message to receiver through app");
            message.setText(messageToSend);
            Transport.send(message);
            Toast.makeText(getContext(), "email send successfull", Toast.LENGTH_SHORT).show();
        } catch (AddressException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/
}
