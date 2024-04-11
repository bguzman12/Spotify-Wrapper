package com.example.cs2340project2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.cs2340project2.WrappedActivity;

import java.util.List;

public class Wrapped1 extends AppCompatActivity {
    private WrappedActivity wrappedActivity;
    private TextView song1, song2, song3, song4, song5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped1);

        // Receive access token from LoginActivity
        String accessToken = getIntent().getStringExtra("accessToken");

        // Pass the access token to WrappedActivity constructor
        wrappedActivity = new WrappedActivity(accessToken);

        song1 = findViewById(R.id.artist1);
        song2 = findViewById(R.id.artist2);
        song3 = findViewById(R.id.artist3);
        song4 = findViewById(R.id.artist4);
        song5 = findViewById(R.id.artist5);

        fetchTopSongs();
    }

    private void fetchTopSongs() {
        // Use the access token to fetch user information
        List<WrappedActivity.SongInfo> songList = wrappedActivity.fetchUserInfo(WrappedActivity.TimeRange.YEAR);

        if (!songList.isEmpty()) {
            song1.setText(songList.get(0).getName());
            song2.setText(songList.get(1).getName());
            song3.setText(songList.get(2).getName());
            song4.setText(songList.get(3).getName());
            song5.setText(songList.get(4).getName());
        }
    }
}
