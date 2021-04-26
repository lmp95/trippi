package com.example.trippi;

import java.io.Serializable;
import java.util.ArrayList;

public class Story implements Serializable {
    public String id;
    public String title;
    public String author;
    public String totalLike;
    public String body;
    public ArrayList<String> images;

    public Story() {
    }

    public Story(String id, String title, String author, String totalLike, String body, ArrayList<String> images) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalLike = totalLike;
        this.body = body;
        this.images = images;
    }
}
