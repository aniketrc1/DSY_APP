package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
        private CardView clgsuugestion;

        private CardView scholar;
         private CardView cutoff;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View root=inflater.inflate(R.layout.fragment_home, container, false);

        clgsuugestion=root.findViewById(R.id.cardSuggestCollege);

        scholar=root.findViewById(R.id.cardscholr);
        cutoff=root.findViewById(R.id.cardSearchCutoff);

        scholar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),Scholarship.class);
                startActivity(i);
            }
        });
        cutoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),cutoff.class);
                startActivity(i);
            }
        });
        clgsuugestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), Suggestion.class);
                startActivity(i);
            }
        });

        return root;
    }
}
