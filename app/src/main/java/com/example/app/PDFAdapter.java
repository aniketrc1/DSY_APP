package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.PDFViewHolder> {

    private ArrayList<PDFItem> pdfList;
    private AppCompatActivity context;

    public PDFAdapter(AppCompatActivity context, ArrayList<PDFItem> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false);
        return new PDFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PDFViewHolder holder, int position) {
        PDFItem item = pdfList.get(position);
        holder.pdfName.setText(item.getName());

        holder.downloadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public static class PDFViewHolder extends RecyclerView.ViewHolder {
        TextView pdfName;
        Button downloadButton;

        public PDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdf_name);
            downloadButton = itemView.findViewById(R.id.download_button);
        }
    }
}
