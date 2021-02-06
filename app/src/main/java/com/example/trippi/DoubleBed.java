package com.example.trippi;

import java.io.Serializable;

public class DoubleBed extends Room implements Serializable {
    public String bedType;
    public String description;
    public DoubleBed(String id, String name, float price) {
        super(id, name, price);
        this.bedType = "Double";
        this.description = "One bed comfortable for 2 persons.";
    }
}
