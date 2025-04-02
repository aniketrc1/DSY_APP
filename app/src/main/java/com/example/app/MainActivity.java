package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
//    database oprtion


    FirebaseAuth f1;
    Toolbar tollBar;
//    TextView t1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        f1=FirebaseAuth.getInstance();
//       FirebaseUser name=f1.getCurrentUser();
//       t1=findViewById(R.id.nameofuser);
        /*databse*/


//        databse end


        tollBar=findViewById(R.id.toolbar2);
        setSupportActionBar(tollBar);
    getSupportActionBar().setTitle("hello Student !");
        BottomNavigationView bottomNavigationView=findViewById(R.id.bootom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment(); // Switch to HomeFragment
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new ProfileFragment(); // Switch to ProfileFragment
            } else if (item.getItemId() == R.id.collage) {
                selectedFragment = new CollegeFragment(); // Switch to CollageFragment
            }

            // Replace the current fragment in fragment_container with the selectedFragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

    }



}