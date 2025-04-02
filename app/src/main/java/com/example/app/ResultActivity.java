package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ListView listView;
    private List<CollegeData> tempCollegeList;
    private CollegeAdapter2 customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = findViewById(R.id.list_view_colleges);
        tempCollegeList = new ArrayList<>();
        customAdapter = new CollegeAdapter2(this, tempCollegeList);
        listView.setAdapter(customAdapter);

        // Get user input from intent
        Intent intent = getIntent();
        String selectedCourse = intent.getStringExtra("course");
        String selectedPlace = intent.getStringExtra("place");
        String selectedCaste = intent.getStringExtra("caste");
        float userMarks = intent.getFloatExtra("marks", 0);

        // Fetch college data from Firebase
        fetchCollegesFromFirebase(selectedCourse, selectedPlace, selectedCaste, userMarks);
    }

    private void fetchCollegesFromFirebase(String course, String place, String caste, float userMarks) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cutoffclg");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempCollegeList.clear();

                // Normalize caste key to match Firebase JSON
                String casteKey = caste.toUpperCase().trim();

                // Map app course names to Firebase branch names
                Map<String, String> branchMap = new HashMap<>();
                branchMap.put("Computer Engineering", "CO");
                branchMap.put("AI Engineering", "AI");
                branchMap.put("Mechanical Engineering", "ME");
                branchMap.put("Civil Engineering", "CE");
                branchMap.put("IT Engineering", "IT");
                branchMap.put("Electronics Engineering", "ENTC");

                String dbBranch = branchMap.getOrDefault(course, course);
                String normalizedPlace = place.trim().toLowerCase();

                Log.d("FirebaseData", "Looking for course: " + dbBranch + ", Place: " + normalizedPlace + ", Caste: " + casteKey);

                for (DataSnapshot collegeSnapshot : snapshot.getChildren()) {
                    String branch = collegeSnapshot.child("Branch").getValue(String.class);
                    String collegeName = collegeSnapshot.child("College_Name").getValue(String.class);
                    String collegePlace = collegeSnapshot.child("Locations").getValue(String.class);
                    String collegeLink = collegeSnapshot.child("Link").getValue(String.class);

                    if (branch == null || collegeName == null || collegePlace == null) {
                        Log.w("FirebaseData", "Skipping due to missing data!");
                        continue;
                    }

                    branch = branch.trim().toUpperCase();
                    collegePlace = collegePlace.trim().toLowerCase();

                    if (!branch.equalsIgnoreCase(dbBranch.trim()) || !collegePlace.equals(normalizedPlace)) {
                        continue;
                    }

                    Double cutoff = null;
                    if (collegeSnapshot.hasChild(casteKey)) {
                        cutoff = collegeSnapshot.child(casteKey).getValue(Double.class);
                    }

                    if (cutoff == null) {
                        Log.e("FirebaseData", "No cutoff for: " + collegeName);
                        continue;
                    }

                    float projectedCutoff = (float) (cutoff + 2.0); // Adding buffer
                    Log.d("FirebaseData", "Checking " + collegeName + " | Cutoff: " + projectedCutoff);

                    if (userMarks >= projectedCutoff) {
                        tempCollegeList.add(new CollegeData(collegeName, projectedCutoff, collegeLink));
                    }
                }

                Collections.sort(tempCollegeList, (a, b) -> Float.compare(b.getCutoff(), a.getCutoff()));

                if (tempCollegeList.isEmpty()) {
                    Toast.makeText(ResultActivity.this, "No colleges found", Toast.LENGTH_SHORT).show();
                }

                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResultActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper class for sorting colleges by cutoff
    public static class CollegeData {
        private final String name;
        private final float cutoff;
        private final String link;

        public CollegeData(String name, float cutoff, String link) {
            this.name = name;
            this.cutoff = cutoff;
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public float getCutoff() {
            return cutoff;
        }

        public String getLink() {
            return link;
        }
    }
}