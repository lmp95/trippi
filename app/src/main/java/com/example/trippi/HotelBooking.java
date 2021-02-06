package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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
    TextView totalPriceTextView;
    Button dateSelectButton;
    MaterialDatePicker datePicker;
    Date fromDate;
    Date toDate;
    String totalDays;
    RadioGroup bedTypeRadioGroup;
    Switch extraBedSwitch;
    float totalPrice;


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
        extraBedSwitch = findViewById(R.id.extraBedSwitch);
        hotelNameTextView.setText(hotel.name);
        hotelTypeTextView.setText(room.name);
        hotelPriceTextView.setText(String.valueOf(room.price));
        MaterialDatePicker.Builder<Pair<Long, Long>> datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        datePicker = datePickerBuilder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                fromDate = new Date(selection.first);
                toDate = new Date(selection.second);
                long different = toDate.getTime() - fromDate.getTime();
                totalDays = String.valueOf(TimeUnit.DAYS.convert(different, TimeUnit.MILLISECONDS));
                hotelBookTotalDay.setText(totalDays + " Days");
                totalPrice = room.price * Float.valueOf(totalDays);
                totalPriceTextView.setText("$ " + totalPrice);
            }
        });
        extraBedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    totalPrice += 35.0;
                    totalPriceTextView.setText("$ " + totalPrice);
                }
                else {
                    totalPrice -= 35.0;
                    totalPriceTextView.setText("$ " + totalPrice);
                }
            }
        });
    }

    public void selectBookingDate(View view) {
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

}