package com.example.cs2340project2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;



public class Wrapped1 extends AppCompatActivity {

    private WrappedActivity wrappedActivity;
    private TextView artist1, artist2, artist3, artist4, artist5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped1);

        //View myBackgroundView = findViewById(R.id.myBackgroundView);
        //myBackgroundView.setBackgroundResource(getRandomDrawable1());
        wrappedActivity = new WrappedActivity();

        artist1 = findViewById(R.id.artist1);
        artist2 = findViewById(R.id.artist2);
        artist3 = findViewById(R.id.artist3);
        artist4 = findViewById(R.id.artist4);
        artist5 = findViewById(R.id.artist5);

        fetchTopSongs();
    }

    private void fetchTopSongs() {
        List<WrappedActivity.SongInfo> songList = wrappedActivity.fetchUserInfo(WrappedActivity.TimeRange.WEEK);

        artist1.setText(songList.get(0).getName());
        artist2.setText(songList.get(1).getName());
        artist3.setText(songList.get(2).getName());
        artist4.setText(songList.get(3).getName());
        artist5.setText(songList.get(4).getName());
    }

    /*
    private int getRandomDrawable1() {
        int[] drawables = new int[] {R.drawable.topsongs1, R.drawable.topsongs2, R.drawable.topsongs3, R.drawable.topsongs4};
        int randomIndex = new Random().nextInt(drawables.length);
        return drawables[randomIndex];
    }

     */

}
