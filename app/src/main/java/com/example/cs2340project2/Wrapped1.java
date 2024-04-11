package com.example.cs2340project2;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import java.util.List;

public class Wrapped1 extends AppCompatActivity implements WrappedActivity.FetchUserInfoListener {
    private WrappedActivity wrappedActivity;
    private TextView artist1, artist2, artist3, artist4, artist5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped1);

        wrappedActivity = new WrappedActivity();
        wrappedActivity.fetchUserInfoAsync(WrappedActivity.TimeRange.YEAR, this);

        artist1 = findViewById(R.id.artist1);
        artist2 = findViewById(R.id.artist2);
        artist3 = findViewById(R.id.artist3);
        artist4 = findViewById(R.id.artist4);
        artist5 = findViewById(R.id.artist5);
    }

    @Override
    public void onUserInfoFetched(List<WrappedActivity.SongInfo> songList) {
        // Check if the songList is not empty and has at least 5 songs
        if (songList != null && songList.size() >= 5) {
            artist1.setText(songList.get(0).getName());
            artist2.setText(songList.get(1).getName());
            artist3.setText(songList.get(2).getName());
            artist4.setText(songList.get(3).getName());
            artist5.setText(songList.get(4).getName());
        } else {
            // Handle the case where there are not enough songs
        }
    }



    /*
    private int getRandomDrawable1() {
        int[] drawables = new int[] {R.drawable.topartist1, R.drawable.topartist2, R.drawable.topartist3, R.drawable.topartist4};
        int randomIndex = new Random().nextInt(drawables.length);
        return drawables[randomIndex];
    }

     */

}
