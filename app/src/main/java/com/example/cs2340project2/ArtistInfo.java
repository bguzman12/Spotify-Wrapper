package com.example.cs2340project2;

public class ArtistInfo {
    private String artist;
    private String imageUrl; // New field for the image URL

    public ArtistInfo(String artist, String imageUrl) {
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public String getArtist() {
        return artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
