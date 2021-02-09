package com.example.trippi;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    public String bookID;
    public Hotel hotel;
    public Room room;
    public String fromDate;
    public String toDate;
    public boolean extraBed;
    public float totalPrice;
    public String userID;
    public String billingCardName;
    public String billingEmail;

    public Booking() {
    }

    public Booking(String bookID, Hotel hotel, Room room, String fromDate,
                   String toDate, boolean extraBed, float totalPrice, String userID,
                   String billingCardName, String billingEmail) {
        this.bookID = bookID;
        this.hotel = hotel;
        this.room = room;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.extraBed = extraBed;
        this.totalPrice = totalPrice;
        this.userID = userID;
        this.billingCardName = billingCardName;
        this.billingEmail = billingEmail;
    }
}
