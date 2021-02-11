package com.example.trippi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookingHistoryAdapter extends ArrayAdapter<Booking> {
    Context context;
    int resource;
    List<Booking> bookingList;

    public BookingHistoryAdapter(@NonNull Context context, int resource, @NonNull List<Booking> bookingList) {
        super(context, resource, bookingList);
        this.context = context;
        this.resource = resource;
        this.bookingList = bookingList;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);
        TextView hotelName = view.findViewById(R.id.historyHotelNameTextView);
        TextView roomName = view.findViewById(R.id.histroyRoomNameTextView);
        TextView fromDate = view.findViewById(R.id.historyHotelFromDateTextView);
        TextView totalPrice = view.findViewById(R.id.historyHotelTotalPriceTextView);

        Booking booking = bookingList.get(position);
        hotelName.setText(booking.hotel.name);
        roomName.setText(booking.room.name);
        fromDate.setText(booking.fromDate);
        totalPrice.setText("$ " + booking.totalPrice);

        return view;
    }
}
