package com.example.trippi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HotelListAdapter extends ArrayAdapter<Hotel> {

    Context context;
    int resource;
    List<Hotel> hotelList;

    public HotelListAdapter(Context context, int resource, List<Hotel> hotelList) {
        super(context, resource, hotelList);
        this.context = context;
        this.resource = resource;
        this.hotelList = hotelList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);
        ImageView imageView = view.findViewById(R.id.hotelImageView);
        TextView hotelName = view.findViewById(R.id.hotelNametextView);
        TextView hotelRating = view.findViewById(R.id.hotelRatingTextView);

        Hotel hotel = hotelList.get(position);
        hotelName.setText(hotel.name);
        hotelRating.setText(String.valueOf(hotel.rating));
//        new LoadHotelImageFromUrl(imageView).execute(hotel.image_url);

        return view;
    }
}
