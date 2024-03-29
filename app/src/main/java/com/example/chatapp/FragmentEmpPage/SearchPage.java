package com.example.chatapp.FragmentEmpPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.ActivityEmpPage.ReaderProfileDetails;
import com.example.chatapp.R;
import com.example.chatapp.activities.ProfileDetailsActivity;
import com.example.chatapp.adapters.RecyclerViewInterface;
import com.example.chatapp.adapters.searchUserAdapter;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.ConsumerProfileDetails;
import com.example.chatapp.utilities.Image;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.UserDetailsRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class SearchPage extends Fragment implements RecyclerViewInterface {
    View view;
    UserDetails userDetails = new UserDetails();
    ConsumerProfileDetails consumerProfileDetails = new ConsumerProfileDetails();
    UserDetailsRecyclerView userDetailsRecyclerView = new UserDetailsRecyclerView();
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    EditText search;
    TextView filter;
    RecyclerView recyclerView;
    ArrayList<UserDetailsRecyclerView> usersArrayList;
    searchUserAdapter myAdapter;
    FirebaseFirestore db;
    Button unreadFilter, readFilter;
    Toast toast;
    Image image = new Image();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_page, container, false);
        search = view.findViewById(R.id.inputSearch);
        readFilter = view.findViewById(R.id.buttonRead);
        unreadFilter = view.findViewById(R.id.buttonUnread);
        filter = view.findViewById(R.id.textFilter);
        progressBar = view.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        db = FirebaseFirestore.getInstance();
        usersArrayList = new ArrayList<UserDetailsRecyclerView>();

        //set data adapter for displaying consumers
        myAdapter = new searchUserAdapter(SearchPage.this.getContext(), usersArrayList, this);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Filter(editable.toString());
            }
        });

        readFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayReadMeter();
            }
        });

        unreadFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUnreadMeter();
            }
        });

        if(userDetails.getUserType().equalsIgnoreCase("Admin")){
            filter.setVisibility(View.GONE);
            readFilter.setVisibility(View.GONE);
            unreadFilter.setVisibility(View.GONE);
        }
        return view;
    }
    //method for searching consumers by serial number
    private void Filter(String toString) {
        ArrayList<UserDetailsRecyclerView> filterList = new ArrayList<>();

        for(UserDetailsRecyclerView users : usersArrayList){
            if(users.getMeterSerialNumber().toLowerCase().contains(search.getText().toString())){
                filterList.add(users);
            }
        }
        myAdapter.setFilteredList(filterList);

    }
    //method for unread meter display
    private void displayUnreadMeter(){
        usersArrayList.clear();
        usersArrayList = new ArrayList<UserDetailsRecyclerView>();

        //set data adapter for displaying consumers
        myAdapter = new searchUserAdapter(SearchPage.this.getContext(), usersArrayList, this);
        recyclerView.setAdapter(myAdapter);

        db.collection("consumers")
                .whereEqualTo("companyId", userDetails.getCompanyID())
                .whereEqualTo("status", "Active")
                .whereEqualTo("remarks", "Unread")
                .orderBy("name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                //usersArrayList.add(dc.getDocument().toObject(UserDetailsRecyclerView.class));
                                UserDetailsRecyclerView user = dc.getDocument().toObject(UserDetailsRecyclerView.class);
                                //progressBar.setVisibility(view.GONE);

                                boolean exists = false;
                                for (UserDetailsRecyclerView u : usersArrayList) {
                                    if (u.getConsId().equals(user.getConsId())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                // Only add the new user if it does not already exist in the list
                                if (!exists) {
                                    usersArrayList.add(user);
                                    progressBar.setVisibility(view.GONE);
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
    }
    //method for read meter display
    private void displayReadMeter(){
        usersArrayList.clear();
        usersArrayList = new ArrayList<UserDetailsRecyclerView>();

        //set data adapter for displaying consumers
        myAdapter = new searchUserAdapter(SearchPage.this.getContext(), usersArrayList, this);
        recyclerView.setAdapter(myAdapter);

        db.collection("consumers")
                .whereEqualTo("companyId", userDetails.getCompanyID())
                .whereEqualTo("status", "Active")
                .whereEqualTo("remarks", "Read")
                .orderBy("name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                //usersArrayList.add(dc.getDocument().toObject(UserDetailsRecyclerView.class));
                                UserDetailsRecyclerView user = dc.getDocument().toObject(UserDetailsRecyclerView.class);
                                //progressBar.setVisibility(view.GONE);

                                boolean exists = false;
                                for (UserDetailsRecyclerView u : usersArrayList) {
                                    if (u.getConsId().equals(user.getConsId())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                // Only add the new user if it does not already exist in the list
                                if (!exists) {
                                    usersArrayList.add(user);
                                    progressBar.setVisibility(view.GONE);
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });

    }
    //method for displaying all consumers
    private void EventChangeListener() {
        db.collection("consumers")
                .whereEqualTo("companyId", userDetails.getCompanyID())
                .whereEqualTo("status", "Active")
                .orderBy("name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                //usersArrayList.add(dc.getDocument().toObject(UserDetailsRecyclerView.class));
                                UserDetailsRecyclerView user = dc.getDocument().toObject(UserDetailsRecyclerView.class);
                                //progressBar.setVisibility(view.GONE);

                                boolean exists = false;
                                for (UserDetailsRecyclerView u : usersArrayList) {
                                    if (u.getConsId().equals(user.getConsId())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                // Only add the new user if it does not already exist in the list
                                if (!exists) {
                                    usersArrayList.add(user);
                                    progressBar.setVisibility(view.GONE);
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
    }
    //method for clicking consumers for data retrieval
    //redirects to ReaderProfileDetails/ProfileDetailsActivity class
    @Override
    public void onItemClick(int position) {
        image.setImage(null);
        if(userDetails.getUserType().equalsIgnoreCase("meter reader")){
            db.collection("users")
                    .whereEqualTo("userId", usersArrayList.get(position).getUserId())
                    .get()
                            .addOnCompleteListener(task ->{
                                DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                                Intent intent;
                                intent = new Intent(this.getContext(), ReaderProfileDetails.class);
                                intent.putExtra("name", usersArrayList.get(position).getName());
                                intent.putExtra("accountNumber", usersArrayList.get(position).getAccountNumber());
                                intent.putExtra("meterStandNumber", usersArrayList.get(position).getMeterStandNumber());
                                intent.putExtra("pumpNumber", usersArrayList.get(position).getPumpNumber());
                                intent.putExtra("tankNumber", usersArrayList.get(position).getTankNumber());
                                intent.putExtra("meterSerialNumber", usersArrayList.get(position).getMeterSerialNumber());
                                intent.putExtra("lineNumber", usersArrayList.get(position).getLineNumber());
                                intent.putExtra("image", usersArrayList.get(position).getImage());
                                intent.putExtra("address", documentUserSnapshot.getString("address"));
                                intent.putExtra("contactNumber", documentUserSnapshot.getString("contactNumber"));
                                intent.putExtra("mail", documentUserSnapshot.getString("email"));
                                intent.putExtra("consID", usersArrayList.get(position).getConsId());
                                startActivity(intent);
                            });
        }
        if(userDetails.getUserType().equalsIgnoreCase("admin")) {
            db.collection("users")
                    .whereEqualTo("userId", usersArrayList.get(position).getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot documentUserSnapshot = task.getResult().getDocuments().get(0);
                        Intent intent;
                        intent = new Intent(this.getContext(), ProfileDetailsActivity.class);
                        intent.putExtra("name", usersArrayList.get(position).getName());
                        intent.putExtra("userID", usersArrayList.get(position).getUserId());
                        intent.putExtra("image", usersArrayList.get(position).getImage());
                        intent.putExtra("consId", usersArrayList.get(position).getConsId());
                        intent.putExtra("address", documentUserSnapshot.getString("address"));
                        intent.putExtra("date", documentUserSnapshot.getString("Date Created"));
                        startActivity(intent);
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        usersArrayList.clear();
        usersArrayList = new ArrayList<UserDetailsRecyclerView>();

        //set data adapter for displaying consumers
        myAdapter = new searchUserAdapter(SearchPage.this.getContext(), usersArrayList, this);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
    }
}