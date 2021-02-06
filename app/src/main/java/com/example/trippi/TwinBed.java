package com.example.trippi;

import java.io.Serializable;

public class TwinBed extends Room implements Serializable {
    public String bedType;
    public String description;
    public TwinBed(String id, String name, float price) {
        super(id, name, price);
        this.bedType = "Twin";
        this.description = "Each person has one bed.";
    }
}
