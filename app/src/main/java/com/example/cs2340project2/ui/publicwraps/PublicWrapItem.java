package com.example.cs2340project2.ui.publicwraps;

public class PublicWrapItem {
    private String imageURL;
    private String date;
    private String time;
    private String author;

    public PublicWrapItem(String imageURL, String date, String time) {
        this.imageURL = imageURL;
        this.date = date;
        this.time = time;
        this.author = "";
    }

    public PublicWrapItem(String imageURL, String date, String time, String author) {
        this.imageURL = imageURL;
        this.date = date;
        this.time = time;
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
