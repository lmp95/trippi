package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HotelBooking extends AppCompatActivity {

    Hotel hotel;
    Room room;
    Booking booking;
    TextView hotelNameTextView;
    TextView hotelTypeTextView;
    TextView hotelPriceTextView;
    TextView hotelBookTotalDay;
    TextView totalPriceTextView;
    TextView extraBedPriceTextView;
    Button dateSelectButton;
    MaterialDatePicker datePicker;
    Date fromDate;
    Date toDate;
    String totalDays;
    RadioGroup bedTypeRadioGroup;
    Switch extraBedSwitch;
    boolean extraBed;
    LinearLayout extraBedLinearLayout;
    float totalPrice;
    float extraBedPrice = (float) 35.0;
    SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_booking);
        hotel = (Hotel) getIntent().getSerializableExtra("BookHotel");
        room = (Room) getIntent().getSerializableExtra("BookRoom");
        hotelNameTextView = findViewById(R.id.bookHotelNameTextView);
        hotelTypeTextView = findViewById(R.id.bookHotelRoomTypeTextView);
        hotelPriceTextView = findViewById(R.id.bookHotelPriceTextView);
        totalPriceTextView = findViewById(R.id.bookHotelTotalPriceNumTextView);
        dateSelectButton = findViewById(R.id.bookHotelSelectDateButton);
        hotelBookTotalDay = findViewById(R.id.bookHotelTotalDaysTextView);
        bedTypeRadioGroup = findViewById(R.id.bedTypeRadioGroup);
        extraBedLinearLayout = findViewById(R.id.extraBedLinearLayout);
        extraBedPriceTextView = findViewById(R.id.extraBedPriceTextView);
        extraBedSwitch = findViewById(R.id.extraBedSwitch);
        hotelNameTextView.setText(hotel.name);
        hotelTypeTextView.setText(room.name);
        hotelPriceTextView.setText(room.price + " per night");
        MaterialDatePicker.Builder<Pair<Long, Long>> datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        datePicker = datePickerBuilder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                fromDate = new Date(selection.first);
                toDate = new Date(selection.second);
                long different = toDate.getTime() - fromDate.getTime();
                totalDays = String.valueOf(TimeUnit.DAYS.convert(different, TimeUnit.MILLISECONDS));
                hotelBookTotalDay.setText(totalDays + " Days");
                totalPrice = (room.price * Float.parseFloat(totalDays)) + extraBedPrice;
                totalPriceTextView.setText("$ " + totalPrice);
            }
        });
        extraBedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                totalPrice += extraBedPrice;
                extraBedLinearLayout.setVisibility(View.VISIBLE);
                extraBed = true;
            }
            else {
                totalPrice -= extraBedPrice;
                extraBedLinearLayout.setVisibility(View.INVISIBLE);
                extraBed = false;
            }
            extraBedPriceTextView.setText("$ " + extraBedPrice);
            totalPriceTextView.setText("$ " + totalPrice);
        });
    }

    public void selectBookingDate(View view) {
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    public void onPaymentButtonClick(View view) {
        booking = new Booking();
        booking.hotelID = hotel.id;
        booking.hotelName = hotel.name;
        booking.roomID = room.id;
        booking.roomType = room.name;
        booking.extraBed = extraBed;
        booking.totalPrice = totalPrice;
        booking.fromDate = simpleDate.format(fromDate);
        booking.toDate = simpleDate.format(toDate);
        Intent intent = new Intent(this, BookingPayment.class);
        intent.putExtra("Booking", booking);
        startActivity(intent);
    }

}