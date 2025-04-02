package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class login extends AppCompatActivity {

    private TextInputEditText logUsername, logPassword;
    private Button loginButton;
    private TextView goToRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        logUsername = findViewById(R.id.loginusername);
        logPassword = findViewById(R.id.loginpassword);
        loginButton = findViewById(R.id.login_btn);
        goToRegister = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        logUsername.requestFocus();
        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = logUsername.getText().toString().trim();
            String password = logPassword.getText().toString().trim();

            // Input validation
            if (email.isEmpty()) {
                logUsername.setError("Email is required");
                logUsername.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                logUsername.setError("Enter a valid email address");
                logUsername.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                logPassword.setError("Password is required");
                logPassword.requestFocus();
                return;
            }

            // Show progress bar while logging in
            progressBar.setVisibility(View.VISIBLE);

            // Authenticate with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Successful login
                            Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Save session in SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userEmail", email);
                            editor.apply();

                            // Navigate to MainActivity
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();  // Close this activity to prevent user from returning to the login screen
                        } else {
                            // Login failed
                            Toast.makeText(login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Navigate to registration screen when user clicks on 'Go to Register'
        goToRegister.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
            finish();  // Close the login activity
        });

        Locale locale = new Locale("en", "US");
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Your other setup code
    }
}