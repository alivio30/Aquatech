package com.example.chatapp.consumerPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.AdminBillingDetails;
import com.example.chatapp.R;
import com.example.chatapp.activities.MainActivity;
import com.example.chatapp.activities.UsersActivity;
import com.example.chatapp.utilities.PreferenceManager;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class CMasterPage extends AppCompatActivity {
    AdminBillingDetails adminBillingDetails = new AdminBillingDetails();
    BottomNavigationView navigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserDetails userDetails = new UserDetails();
    ImageView chatIcon, profile;
    TextView name, position;
    Deque<Integer> integerDeque = new ArrayDeque<>(2);
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmaster_page2);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, adminBillingDetails).commit();
        chatIcon = findViewById(R.id.imageMessenger);
        profile = findViewById(R.id.imageProfile);
        name = findViewById(R.id.textName);
        position = findViewById(R.id.textPosition);

        String imageUrl = null;
        imageUrl = userDetails.getImage();
        Picasso.get().load(imageUrl).into(profile);
        name.setText(userDetails.getName());
        position.setText(userDetails.getUserType());
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });
        navigationView = findViewById(R.id.bottomNavigationView);
        //set navigations for redirect fragment page
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(integerDeque.contains(id)){
                    if(id == R.id.chome){
                        if(integerDeque.size() != 1){
                            if(flag){
                                integerDeque.addFirst(R.id.chome);
                                flag = false;
                            }
                        }
                    }
                    integerDeque.remove(id);
                }
                integerDeque.push(id);
                loadFragment(getFragment(item.getItemId()));
                return false;
            }
        });
    }
    //for navigation bar
    private Fragment getFragment(int itemId) {
        switch(itemId){
            case R.id.chome:
                navigationView.getMenu().getItem(0).setChecked(true);
                return new AdminBillingDetails();
            case R.id.cprofile:
                navigationView.getMenu().getItem(1).setChecked(true);
                return new ConsumerProfileFragment();
        }
        navigationView.getMenu().getItem(0).setChecked(true);
        return new AdminBillingDetails();
    }
    //method for loading fragment
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}