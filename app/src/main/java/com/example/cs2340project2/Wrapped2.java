package com.example.cs2340project2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class Wrapped2 extends AppCompatActivity implements WrappedActivity.FetchUserInfoCallback {

    private WrappedActivity wrappedActivity;
    private TextView song1, song2, song3, song4, song5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped2);

        // Initialize WrappedActivity instance
        wrappedActivity = new WrappedActivity();

        song1 = findViewById(R.id.song1);
        song2 = findViewById(R.id.song2);
        song3 = findViewById(R.id.song3);
        song4 = findViewById(R.id.song4);
        song5 = findViewById(R.id.song5);

        // Fetch top songs
        wrappedActivity.fetchUserInfo(WrappedActivity.TimeRange.YEAR, this);
    }

    @Override
    public void onUserInfoFetched(List<WrappedActivity.SongInfo> songList) {
        // Handle the fetched song list here
        if (songList.size() >= 5) {
            song1.setText(songList.get(0).getName());
            song2.setText(songList.get(1).getName());
            song3.setText(songList.get(2).getName());
            song4.setText(songList.get(3).getName());
            song5.setText(songList.get(4).getName());
        } else {
            // Handle case where fewer than 5 songs are fetched
            // For example, show a message or handle it as needed
        }
    }
}
