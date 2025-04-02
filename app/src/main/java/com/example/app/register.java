package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    TextInputEditText reg_username, reg_password;
    FirebaseAuth f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_password = findViewById(R.id.regpassword);
        reg_username = findViewById(R.id.regusername);
        f1 = FirebaseAuth.getInstance();

        findViewById(R.id.reg_btn).setOnClickListener(v -> {
            String email = reg_username.getText().toString();
            String pass = reg_password.getText().toString();

            f1.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            // Navigate back to login page after successful registration
                            Intent intent = new Intent(register.this, login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(register.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public void onBackPressed() {
        // Override back button to navigate to login page
        Intent intent = new Intent(register.this, login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
