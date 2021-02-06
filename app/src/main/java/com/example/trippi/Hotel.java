package com.example.trippi;

import java.io.Serializable;

public class Hotel implements Serializable {

        public String id;
        public String image_url;
        public String name;
        public String location;
        public float lat;
        public float lng;
        public float rating;

        public Hotel() {
        }

        public Hotel(String id, String image_url, String name, String location, float lat, float lng, float rating) {
            this.id = id;
            this.image_url = image_url;
            this.name = name;
            this.location = location;
            this.lat = lat;
            this.lng = lng;
            this.rating = rating;
        }

}
