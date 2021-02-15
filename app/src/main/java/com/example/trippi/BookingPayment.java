package com.example.trippi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class BookingPayment extends AppCompatActivity {

    Booking booking;
    BillingInformation billingInformation;
    String bookingID;
    TextView hotelNameTextView, roomTypeTextView, fromDateTextView, toDateTextView, totalPriceTextView;
    CheckBox extraBedCheckBox;
    AlertDialog.Builder builder;
    EditText editTextFullName, editTextCardNumber, editTextExpireMonth, editTextExpireYear, editTextCVV, editTextEmail;
    boolean isCreated, isVerified;
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
        hotelNameTextView.setText(booking.hotel.name);
        roomTypeTextView.setText(booking.room.name);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveToDatabase(View view){
        isVerified = checkPaymentForm();
        if(isVerified){
            showDialog("Are you sure to proceed?", "Confirm");
        }
        else{
            Toast.makeText(this, "Please fill all payment information.", Toast.LENGTH_LONG).show();
        }
    }

    public void showDialog(String message, String title){
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    isCreated = createBooking();
                    if(isCreated){
                        dbReference.child("ee7c34b0").child(bookingID).setValue(booking, (error, ref) -> {
                            Intent intent = new Intent(getApplicationContext(), BookingComplete.class);
                            startActivity(intent);
                        });
                    }
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    public boolean checkPaymentForm(){
        if(editTextEmail.getText().length() > 0 && editTextFullName.getText().length() > 0 && editTextCardNumber.getText().length() > 0
        && editTextExpireMonth.getText().length() > 0 && editTextExpireYear.getText().length() > 0 && editTextCVV.getText().length() > 0){
            billingInformation = new BillingInformation(editTextFullName.getText().toString(), editTextEmail.getText().toString());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean createBooking() {
        bookingID = UUID.randomUUID().toString();
        booking.bookID = bookingID;
        booking.billingCardName = editTextFullName.getText().toString();
        booking.billingEmail = editTextEmail.getText().toString();
        return true;
    }

}