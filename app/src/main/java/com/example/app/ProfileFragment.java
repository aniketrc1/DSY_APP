package com.example.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FirebaseAuth f1;
    private TextView t1, emailView, bioView;
    private Button editProfileButton, logoutButton;
    private DatabaseReference userRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        f1 = FirebaseAuth.getInstance();
        FirebaseUser user = f1.getCurrentUser();

        // Firebase Database Reference
        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        }

        t1 = rootView.findViewById(R.id.nameofuser);
        emailView = rootView.findViewById(R.id.profile_email);
        bioView = rootView.findViewById(R.id.profile_bio);
        editProfileButton = rootView.findViewById(R.id.edit_profile_button);
        logoutButton = rootView.findViewById(R.id.logout_button);

        if (user != null) {
            Log.d("ProfileFragment", "User email: " + user.getEmail());
            Log.d("ProfileFragment", "User display name: " + user.getDisplayName());

            // Get user's display name or default fallback
            String userName = user.getDisplayName();
            String email = user.getEmail();

            if (userName != null && !userName.contains("@")) {
                t1.setText(userName);
            } else if (email != null) {
                t1.setText(email.split("@")[0]);
            } else {
                t1.setText("User");
            }

            if (email != null) {
                emailView.setText(email);
            } else {
                emailView.setText("No email provided");
            }

            // Load bio from Firebase (optional, assuming bio is stored in Firebase)
            userRef.child("bio").get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    bioView.setText(snapshot.getValue(String.class));
                } else {
                    bioView.setText("No bio available");
                }
            });
        } else {
            Log.d("ProfileFragment", "No user is signed in.");
            t1.setText("Guest");
            emailView.setText("");
        }

        // Edit Profile Button Click
        editProfileButton.setOnClickListener(v -> showEditProfileDialog());

        // Log Out Button Click
        logoutButton.setOnClickListener(v -> {
            f1.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            getActivity().finish(); // Close the activity after logout
        });

        return rootView;
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Profile");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText editName = view.findViewById(R.id.edit_name);
        EditText editBio = view.findViewById(R.id.edit_bio);

        editName.setText(t1.getText().toString());
        editBio.setText(bioView.getText().toString());

        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = editName.getText().toString().trim();
            String newBio = editBio.getText().toString().trim();

            if (!TextUtils.isEmpty(newName)) {
                t1.setText(newName);
                userRef.child("name").setValue(newName);
            }
            if (!TextUtils.isEmpty(newBio)) {
                bioView.setText(newBio);
                userRef.child("bio").setValue(newBio);
            }

            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
