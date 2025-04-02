package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Scholarship extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private ListView scholarshipListView;
    private ScholarshipAdapter adapter;
    private List<ScholarshipItem> scholarshipList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        scholarshipListView = findViewById(R.id.scholarshipListView);

        scholarshipList = new ArrayList<>();
        adapter = new ScholarshipAdapter(this, scholarshipList);
        scholarshipListView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("fund/Scholarships");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchInput.getText().toString().trim();
                fetchScholarships(query);
            }
        });
    }

    private void fetchScholarships(String query) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scholarshipList.clear();
                boolean found = false;

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String category = categorySnapshot.getKey();

                    if (category.equalsIgnoreCase(query)) {
                        found = true;

                        for (DataSnapshot scholarshipSnapshot : categorySnapshot.getChildren()) {
                            String name = scholarshipSnapshot.child("name").getValue(String.class);
                            String description = scholarshipSnapshot.child("description").getValue(String.class);
                            String link = scholarshipSnapshot.child("link").getValue(String.class);

                            scholarshipList.add(new ScholarshipItem(name, description, link, category));
                        }
                    }
                }

                if (!found) {
                    Toast.makeText(Scholarship.this, "No results found for '" + query + "'", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Scholarship.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ScholarshipItem {
        String name, description, link, category;

        public ScholarshipItem(String name, String description, String link, String category) {
            this.name = name;
            this.description = description;
            this.link = link;
            this.category = category;
        }
    }

    public class ScholarshipAdapter extends ArrayAdapter<ScholarshipItem> {

        public ScholarshipAdapter(@NonNull Context context, @NonNull List<ScholarshipItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.scholarship_item, parent, false);
            }

            final ScholarshipItem scholarship = getItem(position);

            ImageView categoryImage = convertView.findViewById(R.id.categoryImage);
            TextView scholarshipName = convertView.findViewById(R.id.scholarshipName);
            TextView scholarshipDescription = convertView.findViewById(R.id.scholarshipDescription);
            Button applyButton = convertView.findViewById(R.id.applyButton);

            scholarshipName.setText(scholarship.name);
            scholarshipDescription.setText(scholarship.description);

            // Example: Set category image based on category
            if (scholarship.category.equalsIgnoreCase("VJNT")) {
                categoryImage.setImageResource(R.drawable.scholarship); // Add your own images
            } else if (scholarship.category.equalsIgnoreCase("SEBC")) {
                categoryImage.setImageResource(R.drawable.scholarship);
            }else if (scholarship.category.equalsIgnoreCase("OBC")) {
                categoryImage.setImageResource(R.drawable.scholarship);
            }else if (scholarship.category.equalsIgnoreCase("EBC")) {
                categoryImage.setImageResource(R.drawable.scholarship);
            }
            else if (scholarship.category.equalsIgnoreCase("Minority")) {
                categoryImage.setImageResource(R.drawable.scholarship);
            } else if (scholarship.category.equalsIgnoreCase("SC")) {
                categoryImage.setImageResource(R.drawable.scholarship);
            }
            else if (scholarship.category.equalsIgnoreCase("ST")) {
                categoryImage.setImageResource(R.drawable.scholarship);
            }


            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scholarship.link));
                    startActivity(browserIntent);
                }
            });

            return convertView;
        }
    }
}
