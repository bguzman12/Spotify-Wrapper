package com.example.cs2340project2.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WrapData {
    private String timeRange;
    private String date;
    private List<ArtistInfo> topArtists;
    private List<SongInfo> topSongs;
    private boolean isPublic;


    public WrapData(String timeRange, String date, List<ArtistInfo> topArtists, List<SongInfo> topSongs, boolean isPublic) {
        this.timeRange = timeRange;
        this.date = date;
        this.topArtists = topArtists;
        this.topSongs = topSongs;
        this.isPublic = isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isPublic() {
        return isPublic;
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
                ArtistInfo artistInfo = new ArtistInfo(artistData); // Assuming ArtistInfo has a constructor that takes a Map
                topArtistsList.add(artistInfo);
            }
            this.topArtists = topArtistsList;
        }
        if (dataMap.containsKey("topSongs")) {
            List<Map<String, Object>> topSongsData = (List<Map<String, Object>>) dataMap.get("topSongs");
            List<SongInfo> topSongsList = new ArrayList<>();
            for (Map<String, Object> songData : topSongsData) {
                SongInfo songInfo = new SongInfo(songData); // Assuming SongInfo has a constructor that takes a Map
                topSongsList.add(songInfo);
            }
            this.topSongs = topSongsList;
        }
        if (dataMap.containsKey("public")) {
            this.isPublic = (boolean) dataMap.get("public");
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
}
