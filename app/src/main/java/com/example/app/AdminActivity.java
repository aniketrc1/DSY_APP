package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_JSON_FILE = 1;
    private String actionType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button addPdfButton = findViewById(R.id.add_pdf);
        Button addCutoffButton = findViewById(R.id.add_cutoff);
        Button addScholarshipButton = findViewById(R.id.add_scholarship);
        Button logoutButton = findViewById(R.id.logout_button);

        addPdfButton.setOnClickListener(view -> openFileChooser("PDF"));
        addCutoffButton.setOnClickListener(view -> openFileChooser("CUTOFF"));
        addScholarshipButton.setOnClickListener(view -> openFileChooser("SCHOLARSHIP"));
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminActivity.this, login.class)); // replace with actual login activity
            finish();
        });
    }

    private void openFileChooser(String type) {
        actionType = type;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select JSON File"), PICK_JSON_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_JSON_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Toast.makeText(this, actionType + " JSON File Selected", Toast.LENGTH_SHORT).show();

            switch (actionType) {
                case "SCHOLARSHIP":
                    readScholarshipJsonAndUpload(uri);
                    break;
                case "PDF":
                    readPdfJsonAndUpload(uri);
                    break;
                case "CUTOFF":
                    readCutoffJsonAndUpload(uri);
                    break;
            }
        }
    }

    private void readScholarshipJsonAndUpload(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONObject rootObject = new JSONObject(jsonBuilder.toString());

            DatabaseReference baseRef = FirebaseDatabase.getInstance()
                    .getReference("fund")
                    .child("Scholarships");

            Iterator<String> keys = rootObject.keys();

            while (keys.hasNext()) {
                String category = keys.next();
                JSONArray scholarshipsArray = rootObject.getJSONArray(category);
                DatabaseReference categoryRef = baseRef.child(category);

                for (int i = 0; i < scholarshipsArray.length(); i++) {
                    JSONObject scholarship = scholarshipsArray.getJSONObject(i);

                    Map<String, Object> scholarshipMap = new HashMap<>();
                    scholarshipMap.put("name", scholarship.getString("name"));
                    scholarshipMap.put("description", scholarship.getString("description"));
                    scholarshipMap.put("link", scholarship.getString("link"));

                    String id = categoryRef.push().getKey();
                    categoryRef.child(id).setValue(scholarshipMap);
                }
            }

            Toast.makeText(this, "Scholarship data uploaded successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error uploading: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void readPdfJsonAndUpload(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONObject rootObject = new JSONObject(jsonBuilder.toString());
            JSONArray pdfArray = rootObject.getJSONArray("pdfs");

            DatabaseReference pdfRef = FirebaseDatabase.getInstance().getReference("pdf");

            for (int i = 0; i < pdfArray.length(); i++) {
                JSONObject pdf = pdfArray.getJSONObject(i);

                Map<String, Object> pdfMap = new HashMap<>();
                pdfMap.put("title", pdf.getString("title"));
                pdfMap.put("url", pdf.getString("url"));

                String id = pdfRef.push().getKey();
                pdfRef.child(id).setValue(pdfMap);
            }

            Toast.makeText(this, "PDF data uploaded successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error uploading: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void readCutoffJsonAndUpload(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONArray cutoffArray = new JSONArray(jsonBuilder.toString());

            DatabaseReference cutoffRef = FirebaseDatabase.getInstance().getReference("cutoffclg");

            for (int i = 0; i < cutoffArray.length(); i++) {
                JSONObject item = cutoffArray.getJSONObject(i);

                Map<String, Object> cutoffMap = new HashMap<>();
                cutoffMap.put("Sr_No", item.getInt("Sr_No"));
                cutoffMap.put("College_Name", item.getString("College_Name"));
                cutoffMap.put("Dte_code", item.getInt("Dte_code"));
                cutoffMap.put("Branch", item.getString("Branch"));
                cutoffMap.put("OPEN", item.getDouble("OPEN"));
                cutoffMap.put("OBC", item.getDouble("OBC"));
                cutoffMap.put("SC", item.getDouble("SC"));
                cutoffMap.put("ST", item.getDouble("ST"));
                cutoffMap.put("VJNT", item.getDouble("VJNT"));
                cutoffMap.put("EWS", item.getDouble("EWS"));
                cutoffMap.put("SEBC", item.getDouble("SEBC"));
                cutoffMap.put("NT_A", item.getDouble("NT_A"));
                cutoffMap.put("NT_B", item.getDouble("NT_B"));
                cutoffMap.put("NT_C", item.getDouble("NT_C"));
                cutoffMap.put("NT_D", item.getDouble("NT_D"));
                cutoffMap.put("Link", item.getString("Link"));
                cutoffMap.put("Locations", item.getString("Locations"));

                String id = cutoffRef.push().getKey();
                cutoffRef.child(id).setValue(cutoffMap);
            }

            Toast.makeText(this, "Cutoff data uploaded successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error uploading cutoff: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
