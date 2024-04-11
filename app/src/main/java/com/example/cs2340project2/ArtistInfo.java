package com.example.cs2340project2;


public class ArtistInfo {

    private String artist;
    private long listeningTimeInSeconds;

    public ArtistInfo(String artist, long listeningTimeInSeconds) {
        this.artist = artist;
        this.listeningTimeInSeconds = listeningTimeInSeconds;
    }

    public long getListeningTimeInSeconds() {
        return listeningTimeInSeconds;
    }

    public String getArtist() {
        return artist;
    }
}
