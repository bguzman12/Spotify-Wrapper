package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.ui.wraps.WrappedActivity;
import com.example.cs2340project2.utils.ArtistInfo;
import com.example.cs2340project2.utils.SongInfo;
import com.example.cs2340project2.utils.SpotifyAuthentication;
import com.example.cs2340project2.utils.WrapData;
import com.example.cs2340project2.utils.Wrapped;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// TODO: change UI

public class TimeWrapped extends AppCompatActivity {
    private Button pastMonth;
    private Button past6Months;
    private Button pastYear;
    private ImageButton homeBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_wrapped);

        homeBtn = findViewById(R.id.imageButton3);
        pastMonth = findViewById(R.id.past_month_btn);
        past6Months = findViewById(R.id.past_6_month_btn);
        pastYear = findViewById(R.id.past_year_btn);

        pastMonth.setOnClickListener(view -> launchWrapped("short_term"));

        past6Months.setOnClickListener(view -> launchWrapped("medium_term"));

        pastYear.setOnClickListener(view -> launchWrapped("long_term"));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeWrapped.this, Homescreen.class);
                startActivity(intent);
            }
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

                                String date = new SimpleDateFormat("M/d/yy", Locale.getDefault()).format(new Date());
                                intent.putExtra("generatedDate", date);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DocumentReference documentReference = db.collection("pastwraps").document(userID);

                                WrapData wrap = new WrapData(timeRange, date , topArtists, topSongs);
                                Map<String, Object> map = new HashMap<>();

                                final int[] numWraps = new int[1];
                                documentReference.get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            numWraps[0] = document.getData().size();
                                            map.putAll(document.getData());
                                        } else {
                                            numWraps[0] = 0;
                                        }
                                    }

                                    map.put(Integer.toString(numWraps[0]), wrap);
                                    documentReference.set(map);

                                    startActivity(intent);
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

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
