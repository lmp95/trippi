package com.example.trippi;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.ViewHolder> {
    public Context context;
    public ArrayList<Uri> selectedPhotoArrayList;

    public SelectedPhotoAdapter(Context context, ArrayList<Uri> selectedPhotoArrayList) {
        this.context = context;
        this.selectedPhotoArrayList = selectedPhotoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_photo_item, parent, false);
        return new SelectedPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectedPhotoImageView.setImageURI(selectedPhotoArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedPhotoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView selectedPhotoImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedPhotoImageView = itemView.findViewById(R.id.selectedPhotoImageView);
        }
    }
}
