package com.example.trippi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class BookingPayment extends AppCompatActivity {

    Booking booking;
    TextView hotelNameTextView, roomTypeTextView, fromDateTextView, toDateTextView, totalPriceTextView;
    CheckBox extraBedCheckBox;
    AlertDialog.Builder builder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_payment);
        builder = new AlertDialog.Builder(this);
        addBackAction();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        booking = (Booking) getIntent().getSerializableExtra("Booking");
        hotelNameTextView = findViewById(R.id.bookingHotelNameTextView);
        roomTypeTextView = findViewById(R.id.bookingRoomTypeTextView);
        fromDateTextView = findViewById(R.id.bookingFromDateTextView);
        toDateTextView = findViewById(R.id.bookingToDateTextView);
        extraBedCheckBox = findViewById(R.id.bookingExtraBedCheckBox);
        totalPriceTextView = findViewById(R.id.bookingTotalPriceTextView);
        hotelNameTextView.setText(booking.hotelName);
        roomTypeTextView.setText(booking.roomType);
        fromDateTextView.setText(simpleDate.format(booking.fromDate));
        toDateTextView.setText(simpleDate.format(booking.toDate));
        extraBedCheckBox.setChecked(booking.extraBed);
        totalPriceTextView.setText("$ " + booking.totalPrice);
    }

    private void addBackAction() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveToDatabase(View view){
        builder.setMessage("Are you sure to proceed?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //  Action for 'NO' Button
                    dialog.cancel();
                }
            });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirm");
        alert.show();
    }

}