package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WrappedActivity extends AppCompatActivity {

    private TextView song1, song2, song3, song4, song5, artist1, artist2, artist3, artist4, artist5;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private ActivityResultLauncher<Intent> spotifyAuthResLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_wrapped); //this is where it would connect to UI

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

        // ImageViews
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        // Fetch top songs and artists
        spotifyAuthResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
                    if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                        Wrapped wrapped = new Wrapped(response.getAccessToken());

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
                                        // Load images for songs
                                        loadImage(topSongs.get(0).getImageUrl(), imageView1);
                                        loadImage(topSongs.get(1).getImageUrl(), imageView2);
                                        loadImage(topSongs.get(2).getImageUrl(), imageView3);
                                        loadImage(topSongs.get(3).getImageUrl(), imageView4);
                                        loadImage(topSongs.get(4).getImageUrl(), imageView5);
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
                                        loadImage(topArtists.get(1).getImageUrl(), imageView2);
                                        loadImage(topArtists.get(2).getImageUrl(), imageView3);
                                        loadImage(topArtists.get(3).getImageUrl(), imageView4);
                                        loadImage(topArtists.get(4).getImageUrl(), imageView5);
                                    } else {
                                        // Handle case where fewer than 5 artists are fetched
                                        Toast.makeText(WrappedActivity.this, "Must have listened to at least 5 artists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // Handle failure
                            }
                        });
                    }
                });
        spotifyAuthResLauncher.launch(new Intent(AuthorizationClient.createLoginActivityIntent(this, SpotifyAuthentication.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, false))));
    }

    // Helper method to load images into ImageViews
    private void loadImage(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }
}
