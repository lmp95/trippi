package com.example.trippi;

import java.io.Serializable;

public class UserAccount implements Serializable {
    public String uID;
    public String name;
    public String email;
    public String phone;

    public UserAccount() {
    }

    public UserAccount(String uID, String name, String email, String phone) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
