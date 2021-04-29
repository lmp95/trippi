package com.example.trippi;

import java.io.Serializable;

public class ChatGroup implements Serializable {
    public String groupID;
    public String groupName;

    public ChatGroup() {
    }

    public ChatGroup(String groupID, String groupName) {
        this.groupID = groupID;
        this.groupName = groupName;
    }
}
