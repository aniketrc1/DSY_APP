package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CollegeAdapter2 extends BaseAdapter {
    private Context context;
    private List<ResultActivity.CollegeData> collegeList;

    public CollegeAdapter2(Context context, List<ResultActivity.CollegeData> collegeList) {
        this.context = context;
        this.collegeList = collegeList;
    }

    @Override
    public int getCount() {
        return collegeList.size();
    }

    @Override
    public Object getItem(int position) {
        return collegeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_college2, parent, false);
        }

        TextView collegeName = convertView.findViewById(R.id.college_name);
        TextView collegeCutoff = convertView.findViewById(R.id.college_cutoff);
        Button visitButton = convertView.findViewById(R.id.visit_button);

        ResultActivity.CollegeData college = collegeList.get(position);

        collegeName.setText(college.getName());
        collegeCutoff.setText("Cutoff: " + college.getCutoff());

        visitButton.setOnClickListener(v -> {
            if (college.getLink() != null && !college.getLink().isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(college.getLink()));
                context.startActivity(browserIntent);
            }
        });

        return convertView;
    }
}
