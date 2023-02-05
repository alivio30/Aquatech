package com.example.chatapp.consumerPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class ChomeFragment extends Fragment {
    View view;
    UserDetails userDetails = new UserDetails();
    TextView serialNumber, name, address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chome, container, false);
        serialNumber = view.findViewById(R.id.textSerialNo);
        name = view.findViewById(R.id.textName);
        address = view.findViewById(R.id.textConsumerAddress);

        serialNumber.setText(userDetails.getSerialNumber());
        name.setText(userDetails.getName());
        address.setText(userDetails.getAddress());
        return view;
    }


}