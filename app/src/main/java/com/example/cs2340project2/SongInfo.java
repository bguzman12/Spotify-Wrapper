package com.example.cs2340project2;

public class SongInfo {
    private String name;
    private String artist;
    private long listeningTimeInSeconds;

    public SongInfo(String name, String artist, long listeningTimeInSeconds) {
        this.name = name;
        this.artist = artist;
        this.listeningTimeInSeconds = listeningTimeInSeconds;
    }

    public String getName() {
        return name;
    }

    public long getListeningTimeInSeconds() {
        return listeningTimeInSeconds;
    }

    public String getArtist() {
        return artist;
    }
}
