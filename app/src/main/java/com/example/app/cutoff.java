package com.example.app;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cutoff extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PDFAdapter adapter;
    private ArrayList<PDFItem> pdfList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_cutoff);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfList = new ArrayList<>();
        adapter = new PDFAdapter(this, pdfList);
        recyclerView.setAdapter(adapter);

        // Reference to Firebase "pdf" node
        databaseReference = FirebaseDatabase.getInstance().getReference("pdf");

        // Load PDFs from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PDFItem pdfItem = dataSnapshot.getValue(PDFItem.class);
                    if (pdfItem != null) {
                        pdfList.add(pdfItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
