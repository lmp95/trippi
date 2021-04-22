package com.example.trippi;

public class NearbyPlace {
    public String id;
    public String name;
    public String image;
    public float rating;
    public float lat;
    public float lng;

    public NearbyPlace() {
    }

    public NearbyPlace(String id, String name, String image, float rating, float lat, float lng) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
    }
}
