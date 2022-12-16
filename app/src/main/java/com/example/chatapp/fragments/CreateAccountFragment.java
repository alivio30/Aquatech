package com.example.chatapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.chatapp.R;

import java.util.List;

public class CreateAccountFragment extends Fragment {

    Spinner spinner;

    NewMeterNAdminFragment newMeterNAdminFragment;
    NewConsumerFragment newConsumerFragment;
    List<String> names;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}