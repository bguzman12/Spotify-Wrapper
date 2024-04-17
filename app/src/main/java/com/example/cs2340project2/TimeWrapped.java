package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.ui.wraps.WrappedActivity;
import com.example.cs2340project2.utils.ArtistInfo;
import com.example.cs2340project2.utils.SongInfo;
import com.example.cs2340project2.utils.SpotifyAuthentication;
import com.example.cs2340project2.utils.Wrapped;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Request;

// TODO: change UI

public class TimeWrapped extends AppCompatActivity {
    private Button pastMonth;
    private Button past6Months;
    private Button pastYear;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_wrapped);

        pastMonth = findViewById(R.id.past_month_btn);
        past6Months = findViewById(R.id.past_6_month_btn);
        pastYear = findViewById(R.id.past_year_btn);
    }

    @Override
    public void onStart() {
        super.onStart();

        pastMonth.setOnClickListener(view -> {
            launchWrapped("short_term");
        });

        past6Months.setOnClickListener(view -> {
            launchWrapped("medium_term");
        });

        pastYear.setOnClickListener(view -> {
            launchWrapped("long_term");
        });
    }

    private void launchWrapped(String timeRange) {
        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
                Intent intent = new Intent(getBaseContext(), WrappedActivity.class);
                intent.putExtra("timeRange", timeRange);
                wrapped.getTopArtists(timeRange, new Wrapped.TopArtistsCallback() {
                    @Override
                    public void onSuccess(List<ArtistInfo> topArtists) {
                        if (topArtists.size() != 5) {
                            Snackbar.make(findViewById(R.id.time_wrapped_container), "Not enough artists listened to", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        intent.putExtra("topArtists", (Serializable) topArtists);
                        wrapped.getTopSongs(timeRange, new Wrapped.TopSongsCallback() {
                            @Override
                            public void onSuccess(List<SongInfo> topSongs) {
                                if (topSongs.size() != 5) {
                                    Snackbar.make(findViewById(R.id.time_wrapped_container), "Not enough songs listened to", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }
                                intent.putExtra("topSongs", (Serializable) topSongs);
                                intent.putExtra("generatedDate", new SimpleDateFormat("M/d/yy", Locale.getDefault()).format(new Date()));
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String errorMessage) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
