package com.example.trippi;

import java.io.Serializable;
import java.util.List;

public class UserAccount implements Serializable {
    public String uID;
    public String name;
    public String email;
    public String phone;
    public List<String> chatGroup;

    public UserAccount() {
    }

    public UserAccount(String uID, String name, String email, String phone, List<String> chatGroup) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.chatGroup = chatGroup;
    }
}
