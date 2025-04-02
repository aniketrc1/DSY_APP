package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView; // Correct import

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CollegeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CollegeAdapter adapter;
    private List<College> collegeList, filteredList;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collage, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView); // Corrected

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        collegeList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new CollegeAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        fetchColleges();
        setupSearch();

        return view;
    }

    private void fetchColleges() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("clg/Sheet1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collegeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        College college = dataSnapshot.getValue(College.class);
                        if (college != null) {
                            collegeList.add(college);
                        }
                    } catch (Exception e) {
                        Log.e("Firebase", "Data Parsing Error: " + e.getMessage());
                    }
                }
                filteredList.clear();
                filteredList.addAll(collegeList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database Error: " + error.getMessage());
            }
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterColleges(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterColleges(newText);
                return true;
            }
        });
    }

    private void filterColleges(String query) {
        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(collegeList);
        } else {
            for (College college : collegeList) {
                if (college.getCollege_name().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(college);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
