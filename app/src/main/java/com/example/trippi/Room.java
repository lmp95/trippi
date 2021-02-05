package com.example.trippi;

import java.io.Serializable;

public class Room implements Serializable {
    public String id;
    public String name;
    public float price;

    public Room() {
    }

    public Room(String id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
