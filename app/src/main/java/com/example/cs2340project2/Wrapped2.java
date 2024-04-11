package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.List;

public class Wrapped2 extends AppCompatActivity {

    private TextView song1, song2, song3, song4, song5;
    private ActivityResultLauncher<Intent> spotifyAuthResLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped2);

        // Initialize WrappedActivity instance

        song1 = findViewById(R.id.song1);
        song2 = findViewById(R.id.song2);
        song3 = findViewById(R.id.song3);
        song4 = findViewById(R.id.song4);
        song5 = findViewById(R.id.song5);

        // Fetch top songs
        spotifyAuthResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
                    if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                        Wrapped wrapped = new Wrapped(response.getAccessToken());
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
                });
        spotifyAuthResLauncher.launch(new Intent(AuthorizationClient.createLoginActivityIntent(this, SpotifyAuthentication.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, false))));
    }
}
