package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    private TextInputEditText logUsername, logPassword;
    private Button loginButton;
    private TextView goToRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = logUsername.getText().toString().trim();
            String password = logPassword.getText().toString().trim();

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

            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getEmail());
                            }
                        } else {
                            Toast.makeText(login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        goToRegister.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserRole(String email) {
        db.collection("admins").document(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && "admin".equals(document.getString("role"))) {
                            // Admin user
                            navigateToActivity(AdminActivity.class);
                        } else {
                            // Regular user
                            navigateToActivity(MainActivity.class);
                        }
                    } else {
                        Toast.makeText(login.this, "Failed to retrieve role", Toast.LENGTH_SHORT).show();
                        navigateToActivity(MainActivity.class); // Default to user
                    }
                });
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(login.this, activityClass);
        startActivity(intent);
        finish();
    }
}
