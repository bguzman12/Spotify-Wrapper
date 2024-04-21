package com.example.cs2340project2.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrapData {
    private String timeRange;
    private String date;
    private List<ArtistInfo> topArtists;
    private List<SongInfo> topSongs;
    private String author;
    private boolean posted;
    private int position;


    public WrapData(String timeRange, String date, List<ArtistInfo> topArtists, List<SongInfo> topSongs, String author, boolean posted) {
        this.timeRange = timeRange;
        this.date = date;
        this.topArtists = topArtists;
        this.topSongs = topSongs;
        this.author = author;
        this.posted = posted;
    }

    public WrapData(String timeRange, String date, List<ArtistInfo> topArtists, List<SongInfo> topSongs, String author, boolean posted, int position) {
        this.timeRange = timeRange;
        this.date = date;
        this.topArtists = topArtists;
        this.topSongs = topSongs;
        this.author = author;
        this.posted = posted;
        this.position = position;
    }

    public WrapData() {
    }

    public WrapData(Map<String, Object> dataMap) {
        if (dataMap.containsKey("timeRange")) {
            this.timeRange = (String) dataMap.get("timeRange");
        }
        if (dataMap.containsKey("date")) {
            this.date = (String) dataMap.get("date");
        }
        if (dataMap.containsKey("topArtists")) {
            List<Map<String, Object>> topArtistsData = (List<Map<String, Object>>) dataMap.get("topArtists");
            List<ArtistInfo> topArtistsList = new ArrayList<>();
            for (Map<String, Object> artistData : topArtistsData) {
                ArtistInfo artistInfo = new ArtistInfo(artistData);
                topArtistsList.add(artistInfo);
            }
            this.topArtists = topArtistsList;
        }
        if (dataMap.containsKey("topSongs")) {
            List<Map<String, Object>> topSongsData = (List<Map<String, Object>>) dataMap.get("topSongs");
            List<SongInfo> topSongsList = new ArrayList<>();
            for (Map<String, Object> songData : topSongsData) {
                SongInfo songInfo = new SongInfo(songData);
                topSongsList.add(songInfo);
            }
            this.topSongs = topSongsList;
        }

        if (dataMap.containsKey("author")) {
            this.author = (String) dataMap.get("author");
        }

        if (dataMap.containsKey("posted")) {
            this.posted = (Boolean) dataMap.get("posted");
        }

        if (dataMap.containsKey("position")) {
            this.position = Math.toIntExact((long) dataMap.get("position"));
        }
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

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public boolean getPosted() { return posted; }

    public void setPosted(boolean posted) { this.posted = posted; }

    public int getPosition() { return position; }

    public void setPosition(int position) { this.position = position; }
}
