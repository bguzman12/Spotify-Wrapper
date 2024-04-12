package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.List;

public class Wrapped1 extends AppCompatActivity {

    private TextView artist1, artist2, artist3, artist4, artist5;
    private ActivityResultLauncher<Intent> spotifyAuthResLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped1);

        // Initialize WrappedActivity instance

        artist1 = findViewById(R.id.artist1);
        artist2 = findViewById(R.id.artist2);
        artist3 = findViewById(R.id.artist3);
        artist4 = findViewById(R.id.artist4);
        artist5 = findViewById(R.id.artist5);

        // Fetch top songs
        spotifyAuthResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
                    if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                        Wrapped wrapped = new Wrapped(response.getAccessToken());
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
                                    } else {
                                        Toast.makeText(Wrapped1.this, "Must have listened to at least 5 artists", Toast.LENGTH_SHORT).show();
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
}
