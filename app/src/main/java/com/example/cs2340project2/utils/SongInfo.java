package com.example.cs2340project2.utils;

import java.io.Serializable;

public class SongInfo implements Serializable {
    private String name;
    private String artist;
    private String imageUrl; // New field for the image URL
    private String clip;

    public SongInfo(String name, String artist, String imageUrl, String clip) {
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.clip = clip;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getClip() {
        return clip;
    }
}
