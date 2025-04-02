package com.example.app;



import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Suggestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        // UI Elements
        EditText etPercentage = findViewById(R.id.et_percentage);
        Spinner spinnerCourse = findViewById(R.id.spinner_course);
        Spinner spinnerPlace = findViewById(R.id.spinner_place);
        Spinner spinnerCaste = findViewById(R.id.spinner_caste);
//        Spinner spinnerCollegeStatus = findViewById(R.id.spinner_college_status);
        Button btnSearch = findViewById(R.id.btn_search);

        // Data for Spinners
        String[] courses = {"Computer Engineering", "Mechanical Engineering", "Civil Engineering", "AI Engineering", "IT Engineering", "Electronics Engineering"};
        String[] places = {"Pune", "Mumbai", "Sangli", "Nagpur", "Sambhaji Nagar", "Amaravti", "Nanded"};
        String[] castes = {"OPEN", "SC", "ST", "OBC", "NT_A", "NT-B", "NT-C", "NT-D", "VJNTA", "SEBC", "EWS"};
//        String[] collegeStatuses = {"All", "Government", "Private"};

        // Setting Adapters
        setSpinnerAdapter(spinnerCourse, courses);
        setSpinnerAdapter(spinnerPlace, places);
        setSpinnerAdapter(spinnerCaste, castes);
//        setSpinnerAdapter(spinnerCollegeStatus, collegeStatuses);

        // Search Button Click Listener
        btnSearch.setOnClickListener(v -> {
            String selectedCourse = spinnerCourse.getSelectedItem().toString();
            String selectedPlace = spinnerPlace.getSelectedItem().toString();
            String selectedCaste = spinnerCaste.getSelectedItem().toString();
//            String selectedCollegeStatus = spinnerCollegeStatus.getSelectedItem().toString();
            float userMarks = Float.parseFloat(etPercentage.getText().toString());

            // Send Data to ResultActivity
            Intent intent = new Intent(Suggestion.this, ResultActivity.class);
            intent.putExtra("course", selectedCourse);
            intent.putExtra("place", selectedPlace);
            intent.putExtra("caste", selectedCaste);
//            intent.putExtra("collegeStatus", selectedCollegeStatus);
            intent.putExtra("marks", userMarks);
            startActivity(intent);
        });
    }

    private void setSpinnerAdapter(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
