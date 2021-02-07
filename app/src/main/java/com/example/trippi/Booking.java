package com.example.trippi;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    public String bookID;
    public String hotelID;
    public String hotelName;
    public String roomID;
    public String roomType;
    public Date fromDate;
    public Date toDate;
    public boolean extraBed;
    public float totalPrice;
    public String userID;

    public Booking() {
    }

    public Booking(String bookID, String hotelID, String hotelName, String roomID, String roomType, Date fromDate, Date toDate, boolean extraBed, float totalPrice, String userID) {
        this.bookID = bookID;
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.roomID = roomID;
        this.roomType = roomType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.extraBed = extraBed;
        this.totalPrice = totalPrice;
        this.userID = userID;
    }
}
