package com.example.cs2340project2.utils;

import java.io.Serializable;
import java.util.Map;

public class ArtistInfo implements Serializable {
    private String artist;
    private String imageUrl; // New field for the image URL

    public ArtistInfo(String artist, String imageUrl) {
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public ArtistInfo(Map<String, Object> dataMap) {
        if (dataMap.containsKey("artist")) {
            this.artist = (String) dataMap.get("artist");
        }
        if (dataMap.containsKey("imageUrl")) {
            this.imageUrl = (String) dataMap.get("imageUrl");
        }
    }


    public String getArtist() {
        return artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
