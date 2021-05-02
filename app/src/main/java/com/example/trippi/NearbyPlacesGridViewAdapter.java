package com.example.trippi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class NearbyPlacesGridViewAdapter extends ArrayAdapter<NearbyPlace> {
    Context context;
    int resource;
    ArrayList<NearbyPlace> nearbyPlaceArrayList;

    public NearbyPlacesGridViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NearbyPlace> nearbyPlaceArrayList) {
        super(context, resource, nearbyPlaceArrayList);
        this.context = context;
        this.resource = resource;
        this.nearbyPlaceArrayList = nearbyPlaceArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(context).inflate(resource, null);
        NearbyPlace nearbyPlace = getItem(position);
        ImageView nearbyPlaceImageView = view.findViewById(R.id.nearbyPlaceImg);
        TextView nearbyPlaceTextView = view.findViewById(R.id.nearbyPlaceTextView);
        TextView nearbyPlaceRatingTextView = view.findViewById(R.id.nearbyPlaceRatingTextView);
        RatingBar nearbyPlaceRatingBar = view.findViewById(R.id.nearbyPlaceRatingBar);
        nearbyPlaceTextView.setText(nearbyPlace.name);
        nearbyPlaceRatingBar.setRating(nearbyPlace.rating);
        nearbyPlaceRatingTextView.setText(String.valueOf(nearbyPlace.rating));
        Glide.with(context)
                .load(nearbyPlace.image)
                .centerCrop()
                .into(nearbyPlaceImageView);
        return view;
    }
}
