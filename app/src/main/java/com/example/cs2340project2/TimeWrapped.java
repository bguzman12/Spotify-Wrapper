package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cs2340project2.ui.wraps.WrappedActivity;
import com.example.cs2340project2.utils.ArtistInfo;
import com.example.cs2340project2.utils.SongInfo;
import com.example.cs2340project2.utils.SpotifyAuthentication;
import com.example.cs2340project2.utils.WrapData;
import com.example.cs2340project2.utils.Wrapped;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TimeWrapped extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private AutoCompleteTextView time;
    private MaterialSwitch publicSwitch;
    private ConstraintLayout publicAccess;
    private FloatingActionButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timewrapped);

        toolbar = findViewById(R.id.topAppBar_timeWrapped);
        time = findViewById(R.id.time);
        publicSwitch = findViewById(R.id.public_switch);
        publicAccess = findViewById(R.id.public_access_container);
        next = findViewById(R.id.next_fab);

        toolbar.setNavigationOnClickListener(listener -> finish());

        publicAccess.setOnClickListener(listener -> publicSwitch.setChecked(!publicSwitch.isChecked()));

        next.setOnClickListener(view -> {
            next.setEnabled(false);
            String timeRange = switch (time.getText().toString()) {
                default -> "short_term";
                case "Last 6 Months" -> "medium_term";
                case "Last Year" -> "long_term";
            };
            launchWrapped(timeRange, publicSwitch.isChecked());
        });

    }

    private void launchWrapped(String timeRange, boolean isPublic) {
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
                                boolean post = publicSwitch.isChecked();
                                intent.putExtra("generatedDate", date);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                db.collection("tokens").document(userID).update("posts", FieldValue.increment(1));

                                if (post) {
                                    db.collection("tokens").document(userID).update("public_posts", FieldValue.increment(1));
                                }

                                DocumentReference documentReference = db.collection("pastwraps").document(userID);

                                WrapData wrap = new WrapData(timeRange, date , topArtists, topSongs, userID, post, 0);

                                Map<String, Object> map = new HashMap<>();

                                documentReference.get().addOnCompleteListener(task -> {
                                    int numWraps = 0;
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            numWraps = document.getData().size();
                                            map.putAll(document.getData());
                                        }
                                    }

                                    wrap.setPosition(numWraps);
                                    map.put(Integer.toString(numWraps), wrap);
                                    documentReference.set(map);

                                    if (post) {
                                        DocumentReference newDocumentReference = db.collection("pastwraps").document("public");
                                        Map<String, Object> pubMap = new HashMap<>();
                                        newDocumentReference.get().addOnCompleteListener(pubTask -> {
                                            int pubWraps = 0;
                                            if (pubTask.isSuccessful()) {
                                                DocumentSnapshot newDocument = pubTask.getResult();
                                                if (newDocument.exists()) {
                                                    pubWraps = newDocument.getData().size();
                                                }
                                            }

                                            pubMap.put(Integer.toString(pubWraps), wrap);
                                            newDocumentReference.set(pubMap);
                                        });
                                    }

                                    TimeWrapped.this.finish();
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
