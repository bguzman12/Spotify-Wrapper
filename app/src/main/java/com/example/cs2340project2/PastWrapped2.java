package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.widget.ImageButton;


import java.util.List;

public class PastWrapped2 extends AppCompatActivity {

    private TextView song1, song2, song3, song4, song5;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private ImageButton wrapped2_next_btn, wrapped2_back_btn;

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
            startActivity(new Intent(this, Comb_wrap.class));
        });

        wrapped2_back_btn.setOnClickListener(view -> {
            startActivity(new Intent(this, PastWrapped1.class));
        });

        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
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
}
