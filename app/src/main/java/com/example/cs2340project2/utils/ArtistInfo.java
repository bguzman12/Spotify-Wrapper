package com.example.cs2340project2.utils;

import java.io.Serializable;

public class ArtistInfo implements Serializable {
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
