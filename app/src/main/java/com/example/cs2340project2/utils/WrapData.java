package com.example.cs2340project2.utils;

import java.util.List;

public class WrapData {
    private String timeRange;
    private String date;
    private List<ArtistInfo> topArtists;
    private List<SongInfo> topSongs;


    public WrapData(String timeRange, String date, List<ArtistInfo> topArtists, List<SongInfo> topSongs) {
        this.timeRange = timeRange;
        this.date = date;
        this.topArtists = topArtists;
        this.topSongs = topSongs;
    }

    public WrapData() {
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ArtistInfo> getTopArtists() {
        return topArtists;
    }

    public void setTopArtists(List<ArtistInfo> topArtists) {
        this.topArtists = topArtists;
    }

    public List<SongInfo> getTopSongs() {
        return topSongs;
    }

    public void setTopSongs(List<SongInfo> topSongs) {
        this.topSongs = topSongs;
    }
}
