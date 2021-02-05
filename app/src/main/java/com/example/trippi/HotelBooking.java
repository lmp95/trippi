package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HotelBooking extends AppCompatActivity {

    Hotel hotel;
    Room room;
    TextView hotelNameTextView;
    TextView hotelTypeTextView;
    TextView hotelPriceTextView;
    TextView hotelBookTotalDay;
    Button dateSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_booking);
        hotel = (Hotel) getIntent().getSerializableExtra("BookHotel");
        room = (Room) getIntent().getSerializableExtra("BookRoom");
        hotelNameTextView = findViewById(R.id.bookHotelNameTextView);
        hotelTypeTextView = findViewById(R.id.bookHotelRoomTypeTextView);
        hotelPriceTextView = findViewById(R.id.bookHotelPriceTextView);
        dateSelectButton = findViewById(R.id.bookHotelSelectDateButton);
        hotelBookTotalDay = findViewById(R.id.bookHotelTotalDaysTextView);
        hotelNameTextView.setText(hotel.name);
        hotelTypeTextView.setText(room.name);
        hotelPriceTextView.setText(String.valueOf(room.price));

    }
}