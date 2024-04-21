package com.example.cs2340project2;

public class SongInfo {
    private String name;
    private String artist;
    private long listeningTimeInSeconds;
    private String imageUrl; // New field for the image URL

    public SongInfo(String name, String artist, String imageUrl, long listeningTimeInSeconds) {
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.listeningTimeInSeconds = listeningTimeInSeconds;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public long getListeningTimeInSeconds() {
        return listeningTimeInSeconds;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
