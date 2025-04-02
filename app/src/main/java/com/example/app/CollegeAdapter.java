package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.ViewHolder> {

    private List<College> collegeList;
    private Context context;

    public CollegeAdapter(Context context, List<College> collegeList) {
        this.context = context;
        this.collegeList = collegeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        College college = collegeList.get(position);
        holder.tvDteCode.setText(college.getDte_code());
        holder.tvCollegeName.setText(college.getCollege_name());

        // Handle click event to open college link in browser
        holder.tvCollegeName.setOnClickListener(v -> {
            String url = college.getLink();
            if (url != null && !url.isEmpty()) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;  // Ensure URL has a valid scheme
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collegeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDteCode, tvCollegeName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDteCode = itemView.findViewById(R.id.tvDteCode);
            tvCollegeName = itemView.findViewById(R.id.tvCollegeName);
        }
    }
}
