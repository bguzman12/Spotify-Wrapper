package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.widget.ImageButton;

import java.util.List;

public class Wrapped1 extends AppCompatActivity {

    private TextView artist1, artist2, artist3, artist4, artist5;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;

    private ImageButton wrapped1_next_btn;

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

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        wrapped1_next_btn = findViewById(R.id.wrapped1_next_btn);

        wrapped1_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wrapped1.this, Wrapped2.class);
                startActivity(intent);
            }
        });

        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
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
                                String imageUrlString1 = topArtists.get(0).getImageUrl();
                                String imageUrlString2 = topArtists.get(1).getImageUrl();
                                String imageUrlString3 = topArtists.get(2).getImageUrl();
                                String imageUrlString4 = topArtists.get(3).getImageUrl();
                                String imageUrlString5 = topArtists.get(4).getImageUrl();
                                Picasso.get().load(imageUrlString1).into(imageView1);
                                Picasso.get().load(imageUrlString2).into(imageView2);
                                Picasso.get().load(imageUrlString3).into(imageView3);
                                Picasso.get().load(imageUrlString4).into(imageView4);
                                Picasso.get().load(imageUrlString5).into(imageView5);

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

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
