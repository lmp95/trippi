package com.example.trippi;

import java.io.Serializable;

public class Booking implements Serializable {
    public String bookID;
    public String hotelID;
    public String roomID;
    public boolean extraBed;
    public float totalPrice;
    public boolean isPaid;
    public String userID;

    public Booking() {
    }

    public Booking(String bookID, String hotelID, String roomID, boolean extraBed, float totalPrice, boolean isPaid, String userID) {
        this.bookID = bookID;
        this.hotelID = hotelID;
        this.roomID = roomID;
        this.extraBed = extraBed;
        this.totalPrice = totalPrice;
        this.isPaid = isPaid;
        this.userID = userID;
    }
}
