package com.example.trippi;

import java.io.Serializable;

public class NearbyPlace implements Serializable {
    public String id;
    public String name;
    public String image;
    public String address;
    public String phone;
    public float rating;
    public float lat;
    public float lng;

    public NearbyPlace() {
    }

    public NearbyPlace(String id, String name, String image, String address, String phone, float rating, float lat, float lng) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
    }
}
