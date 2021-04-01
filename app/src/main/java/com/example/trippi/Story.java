package com.example.trippi;

public class Story {
    public String id;
    public String title;
    public String author;
    public String totalLike;
    public String body;

    public Story() {
    }

    public Story(String id, String title, String author, String totalLike, String body) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalLike = totalLike;
        this.body = body;
    }
}
