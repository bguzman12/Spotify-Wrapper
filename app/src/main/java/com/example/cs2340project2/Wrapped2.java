package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Random;


public class Wrapped2 extends AppCompatActivity {
    private WrappedActivity wrappedActivity;
    private TextView song1, song2, song3, song4, song5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped2);

        //View myBackgroundView = findViewById(R.id.myBackgroundView);
        //myBackgroundView.setBackgroundResource(getRandomDrawable1());
        wrappedActivity = new WrappedActivity();

        song1 = findViewById(R.id.song1);
        song2 = findViewById(R.id.song2);
        song3 = findViewById(R.id.song3);
        song4 = findViewById(R.id.song4);
        song5 = findViewById(R.id.song5);

        fetchTopSongs();
    }

    private void fetchTopSongs() {
        List<WrappedActivity.SongInfo> songList = wrappedActivity.fetchUserInfo(WrappedActivity.TimeRange.WEEK);

        song1.setText(songList.get(0).getName());
        song2.setText(songList.get(1).getName());
        song3.setText(songList.get(2).getName());
        song4.setText(songList.get(3).getName());
        song5.setText(songList.get(4).getName());
    }

    /*
    private int getRandomDrawable1() {
        int[] drawables = new int[] {R.drawable.topsongs1, R.drawable.topsongs2, R.drawable.topsongs3, R.drawable.topsongs4};
        int randomIndex = new Random().nextInt(drawables.length);
        return drawables[randomIndex];
    }

     */
}

