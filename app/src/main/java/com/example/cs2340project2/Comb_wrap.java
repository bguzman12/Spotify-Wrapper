package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Comb_wrap extends AppCompatActivity {

    private TextView song1, song2, song3, song4, song5, artist1, artist2, artist3, artist4, artist5, topGenre;
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped_summary); //this is where it would connect to UI

        // Initialize WrappedActivity instance

        // Songs
        song1 = findViewById(R.id.song1);
        song2 = findViewById(R.id.song2);
        song3 = findViewById(R.id.song3);
        song4 = findViewById(R.id.song4);
        song5 = findViewById(R.id.song5);

        // Artists
        artist1 = findViewById(R.id.artist1);
        artist2 = findViewById(R.id.artist2);
        artist3 = findViewById(R.id.artist3);
        artist4 = findViewById(R.id.artist4);
        artist5 = findViewById(R.id.artist5);

        //Genre
        topGenre = findViewById(R.id.top_genre);

        // ImageViews
        imageView1 = findViewById(R.id.imageView1);

        // Fetch top songs and artists

    }

    @Override
    public void onStart() {
        super.onStart();

        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);

                // Fetch top songs
                wrapped.getTopSongs(Wrapped.TimeRange.MONTH, new Wrapped.TopSongsCallback() {
                    @Override
                    public void onSuccess(List<SongInfo> topSongs) {
                        runOnUiThread(() -> {
                            if (topSongs.size() >= 5) {
                                song1.setText(topSongs.get(0).getName());
                                song2.setText(topSongs.get(1).getName());
                                song3.setText(topSongs.get(2).getName());
                                song4.setText(topSongs.get(3).getName());
                                song5.setText(topSongs.get(4).getName());
                            } else {
                                // Handle case where fewer than 5 songs are fetched
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }
                });

                // Fetch top artists
                wrapped.getTopArtists(Wrapped.TimeRange.MONTH, new Wrapped.TopArtistsCallback() {
                    @Override
                    public void onSuccess(List<ArtistInfo> topArtists) {
                        runOnUiThread(() -> {
                            if (topArtists.size() >= 5) {
                                artist1.setText(topArtists.get(0).getArtist());
                                artist2.setText(topArtists.get(1).getArtist());
                                artist3.setText(topArtists.get(2).getArtist());
                                artist4.setText(topArtists.get(3).getArtist());
                                artist5.setText(topArtists.get(4).getArtist());
                                // Load images for artists
                                loadImage(topArtists.get(0).getImageUrl(), imageView1);
                            } else {
                                // Handle case where fewer than 5 artists are fetched
                                Toast.makeText(Comb_wrap.this, "Must have listened to at least 5 artists", Toast.LENGTH_SHORT).show();
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

    // Helper method to load images into ImageViews
    private void loadImage(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }
}
