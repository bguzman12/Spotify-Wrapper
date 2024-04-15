package com.example.cs2340project2;

public class SongInfo {
    private String name;
    private String artist;
    private String uri;
    private long listeningTimeInSeconds;
    private String imageUrl; // New field for the image URL

    public SongInfo(String name, String artist, String uri, String imageUrl, long listeningTimeInSeconds) {
        this.name = name;
        this.artist = artist;
        this.uri = uri;
        this.imageUrl = imageUrl;
        this.listeningTimeInSeconds = listeningTimeInSeconds;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getUri(){
        return uri;
    }

    public long getListeningTimeInSeconds() {
        return listeningTimeInSeconds;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
