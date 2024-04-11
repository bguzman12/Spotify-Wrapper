package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.List;

public class Wrapped1 extends AppCompatActivity {

    private TextView song1, song2, song3, song4, song5;
    private ActivityResultLauncher<Intent> spotifyAuthResLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped1);

        // Initialize WrappedActivity instance


        song1 = findViewById(R.id.artist1);
        song2 = findViewById(R.id.artist2);
        song3 = findViewById(R.id.artist3);
        song4 = findViewById(R.id.artist4);
        song5 = findViewById(R.id.artist5);

        spotifyAuthResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
                    if (response.getType() == AuthorizationResponse.Type.TOKEN) {

                    }
                });
        spotifyAuthResLauncher.launch(new Intent(AuthorizationClient.createLoginActivityIntent(this, SpotifyAuthentication.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, false))));
        // Fetch top songs

    }
}
