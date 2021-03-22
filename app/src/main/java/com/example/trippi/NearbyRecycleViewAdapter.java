package com.example.trippi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NearbyRecycleViewAdapter extends RecyclerView.Adapter<NearbyRecycleViewAdapter.ViewHolder> {
    Context context;
    ArrayList<NearbyPlace> nearbyPlaceArrayList;
    OnNearbyPlaceClickListener listener;
    int selectedItem;

    public NearbyRecycleViewAdapter(Context context, ArrayList<NearbyPlace> nearbyPlaceArrayList, OnNearbyPlaceClickListener onNearbyPlaceClickListener) {
        this.context = context;
        this.nearbyPlaceArrayList = nearbyPlaceArrayList;
        this.listener = onNearbyPlaceClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_places_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nearbyPlaceTextView.setText(nearbyPlaceArrayList.get(position).name);
//        holder.nearbyPlaceImageView.setImageResource(R.drawable.ic_launcher_background);
        holder.nearbyPlaceRatingTextView.setText(String.valueOf(nearbyPlaceArrayList.get(position).rating));
        holder.nearbyPlaceRatingBar.setRating(nearbyPlaceArrayList.get(position).rating);
        new DownloadHotelImageFromUrl(holder.nearbyPlaceImageView).execute(nearbyPlaceArrayList.get(position).image);
    }

    @Override
    public int getItemCount() {
        return nearbyPlaceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nearbyPlaceTextView;
        ImageView nearbyPlaceImageView;
        TextView nearbyPlaceRatingTextView;
        RatingBar nearbyPlaceRatingBar;
        OnNearbyPlaceClickListener onNearbyPlaceClickListener;

        public ViewHolder(@NonNull View itemView, OnNearbyPlaceClickListener onNearbyPlaceClickListener) {
            super(itemView);
            this.onNearbyPlaceClickListener = onNearbyPlaceClickListener;
            nearbyPlaceImageView = itemView.findViewById(R.id.nearbyPlaceImg);
            nearbyPlaceTextView = itemView.findViewById(R.id.nearbyPlaceTextView);
            nearbyPlaceRatingTextView = itemView.findViewById(R.id.nearbyPlaceRatingTextView);
            nearbyPlaceRatingBar = itemView.findViewById(R.id.nearbyPlaceRatingBar);
        }
    }

    public interface OnNearbyPlaceClickListener{
        void onNearbyPlaceClick(int position);
    }
}
