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
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class BookingPayment extends AppCompatActivity {

    Booking booking;
    String bookingID;
    TextView hotelNameTextView, roomTypeTextView, fromDateTextView, toDateTextView, totalPriceTextView;
    CheckBox extraBedCheckBox;
    AlertDialog.Builder builder;
    EditText editTextFullName, editTextCardNumber, editTextExpireMonth, editTextExpireYear, editTextCVV, editTextEmail;
    boolean isCreated;
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("Booking");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_payment);
        builder = new AlertDialog.Builder(this);
        addBackAction();
        booking = (Booking) getIntent().getSerializableExtra("Booking");
        hotelNameTextView = findViewById(R.id.bookingHotelNameTextView);
        roomTypeTextView = findViewById(R.id.bookingRoomTypeTextView);
        fromDateTextView = findViewById(R.id.bookingFromDateTextView);
        toDateTextView = findViewById(R.id.bookingToDateTextView);
        extraBedCheckBox = findViewById(R.id.bookingExtraBedCheckBox);
        totalPriceTextView = findViewById(R.id.bookingTotalPriceTextView);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpireMonth = findViewById(R.id.editTextExpireMonth);
        editTextExpireYear = findViewById(R.id.editTextExpireYear);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextEmail = findViewById(R.id.editTextEmail);
        hotelNameTextView.setText(booking.hotelName);
        roomTypeTextView.setText(booking.roomType);
        fromDateTextView.setText(booking.fromDate);
        toDateTextView.setText(booking.toDate);
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
                    isCreated = createBooking();
                    if(isCreated){
                        dbReference.child(bookingID).setValue(booking);
                    }
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

    private boolean createBooking() {
        bookingID = UUID.randomUUID().toString();
        booking.bookID = bookingID;
        booking.billingCardName = editTextFullName.getText().toString();
        booking.billingEmail = editTextEmail.getText().toString();
        booking.userID = "123FGEFDW";
        return true;
    }

}