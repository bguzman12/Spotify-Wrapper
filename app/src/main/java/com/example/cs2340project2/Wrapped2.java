package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.widget.ImageButton;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.SpotifyAppRemote;




import java.util.List;

public class Wrapped2 extends AppCompatActivity {
    private SpotifyAppRemote mSpotifyAppRemote;
    private TextView song1, song2, song3, song4, song5;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private ImageButton wrapped2_next_btn, wrapped2_back_btn;
    private Wrapped.TimeRange timeRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped2);
        timeRange = Wrapped.TimeRange.valueOf(getIntent().getStringExtra("time"));

        // Initialize WrappedActivity instance

        song1 = findViewById(R.id.song1);
        song2 = findViewById(R.id.song2);
        song3 = findViewById(R.id.song3);
        song4 = findViewById(R.id.song4);
        song5 = findViewById(R.id.song5);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        wrapped2_next_btn = findViewById(R.id.wrapped2_next_btn);
        wrapped2_back_btn = findViewById(R.id.wrapped2_back_btn);

        // Fetch top songs
    }

    @Override
    public void onStart() {
        super.onStart();

        wrapped2_next_btn.setOnClickListener(view -> {
            startActivity(new Intent(this, Comb_wrap.class).putExtra("time", timeRange.toString()));
        });

        wrapped2_back_btn.setOnClickListener(view -> {
            finish();
        });

        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
                wrapped.getTopSongs(timeRange, new Wrapped.TopSongsCallback() {
                    @Override
                    public void onSuccess(List<SongInfo> topSongs) {
                        runOnUiThread(() -> {
                            if (topSongs.size() >= 5) {
                                // Display song names and images
                                song1.setText(topSongs.get(0).getName());
                                song2.setText(topSongs.get(1).getName());
                                song3.setText(topSongs.get(2).getName());
                                song4.setText(topSongs.get(3).getName());
                                song5.setText(topSongs.get(4).getName());
                                String imageUrlString1 = topSongs.get(0).getImageUrl();
                                String imageUrlString2 = topSongs.get(1).getImageUrl();
                                String imageUrlString3 = topSongs.get(2).getImageUrl();
                                String imageUrlString4 = topSongs.get(3).getImageUrl();
                                String imageUrlString5 = topSongs.get(4).getImageUrl();
                                Picasso.get().load(imageUrlString1).into(imageView1);
                                Picasso.get().load(imageUrlString2).into(imageView2);
                                Picasso.get().load(imageUrlString3).into(imageView3);
                                Picasso.get().load(imageUrlString4).into(imageView4);
                                Picasso.get().load(imageUrlString5).into(imageView5);

                                // Add button click listeners for each song
                                song1.setOnClickListener(v -> playSongClip(topSongs.get(0).getUri()));
                                song2.setOnClickListener(v -> playSongClip(topSongs.get(1).getUri()));
                                song3.setOnClickListener(v -> playSongClip(topSongs.get(2).getUri()));
                                song4.setOnClickListener(v -> playSongClip(topSongs.get(3).getUri()));
                                song5.setOnClickListener(v -> playSongClip(topSongs.get(4).getUri()));
                            } else {
                                // Handle case where fewer than 5 songs are fetched
                                // For example, show a message or handle it as needed
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void playSongClip(String songUri) {
        // Play the song clip using the song URI
        // This is where you would integrate the Spotify SDK or any other music player API
    }

}
