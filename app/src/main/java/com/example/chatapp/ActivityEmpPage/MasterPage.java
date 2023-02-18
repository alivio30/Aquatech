package com.example.chatapp.ActivityEmpPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.FragmentEmpPage.HomePage;
import com.example.chatapp.FragmentEmpPage.ProfilePage;
import com.example.chatapp.FragmentEmpPage.ReaderProfileFragment;
import com.example.chatapp.FragmentEmpPage.SearchPage;
import com.example.chatapp.R;
import com.example.chatapp.activities.MainActivity;
import com.example.chatapp.activities.UsersActivity;
import com.example.chatapp.databinding.ActivityMasterPageBinding;
import com.example.chatapp.utilities.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

public class MasterPage extends AppCompatActivity {
    ActivityMasterPageBinding binding;
    BottomNavigationView bottomNavigationView;
    ImageView chatIcon, profile;
    TextView userType, userName;
    HomePage homePage = new HomePage();
    UserDetails userDetails = new UserDetails();
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterPageBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_master_page);
        userType = findViewById(R.id.textPosition);
        profile = findViewById(R.id.imageProfile);
        userName = findViewById(R.id.textName);

        //display image profile
        String imageUrl = null;
        imageUrl = userDetails.getImage();
        Picasso.get().load(imageUrl).into(profile);

        //set text if user is reader or admin
        if(userDetails.getUserType().equalsIgnoreCase("admin")) userType.setText("Admin");
        if(userDetails.getUserType().equalsIgnoreCase("meter reader")) userType.setText("Meter Reader");
        userName.setText(userDetails.getName());

        //chat icon
        chatIcon = findViewById(R.id.imageMessenger);
        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homePage).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(integerDeque.contains(id)){
                    if(id == R.id.home){
                        if(integerDeque.size() != 1){
                            if(flag){
                                integerDeque.addFirst(R.id.home);
                                flag = false;
                            }
                        }
                    }
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
            case R.id.homebar:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomePage();
            case R.id.searchbar:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new SearchPage();
            case R.id.profilebar:
                if(userDetails.getUserType().equalsIgnoreCase("meter reader")){
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    return new ReaderProfileFragment();
                }
                if(userDetails.getUserType().equalsIgnoreCase("admin")){
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    return new ProfilePage();
                }
        }
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        return new HomePage();
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}