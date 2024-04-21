package com.example.cs2340project2.utils;

import android.media.MediaPlayer;
import android.os.CountDownTimer;

import androidx.lifecycle.ViewModel;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class WrapViewModel extends ViewModel {
    private List<SongInfo> topSongs;
    private List<ArtistInfo> topArtists;
    private String timeRange;
    private MediaPlayer mediaPlayer;
    private String generatedDate;
    private boolean paused;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public void setTopArtists(List<ArtistInfo> topArtists) {
        this.topArtists = topArtists;
    }

    public void setTopSongs(List<SongInfo> topSongs) {
        this.topSongs = topSongs;
    }

    public List<ArtistInfo> getTopArtists() {
        return topArtists;
    }

    public List<SongInfo> getTopSongs() {
        return topSongs;
    }

    public String getTimeRange() {
        return timeRange;
    }
}
