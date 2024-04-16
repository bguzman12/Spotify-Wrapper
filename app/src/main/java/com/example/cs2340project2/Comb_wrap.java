package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.view.View;
import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import android.graphics.Bitmap.CompressFormat;
import android.widget.ImageButton;
import android.graphics.Canvas;

import android.os.Build;
import android.provider.MediaStore;
import android.os.Environment;
import java.io.OutputStream;
import android.net.Uri;
import androidx.core.content.FileProvider;
import android.content.ContentValues;





import java.util.List;

public class Comb_wrap extends AppCompatActivity {

    private TextView song1, song2, song3, song4, song5, artist1, artist2, artist3, artist4, artist5, topGenre, minsListened;
    private ImageView imageView1;
    public ImageButton exportBtn;
    private Wrapped.TimeRange timeRange;

    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped_summary); //this is where it would connect to UI
        timeRange = Wrapped.TimeRange.valueOf(getIntent().getStringExtra("time"));

        ImageButton exportBtn = findViewById(R.id.exportBtn);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScreen();
            }
        });


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

        // Genre
        topGenre = findViewById(R.id.top_genre);

        // Time
        minsListened = findViewById(R.id.mins_listened);

        // ImageViews
        imageView1 = findViewById(R.id.imageView1);

        // Button
        backBtn = findViewById(R.id.wrapped2_back_btn2);

        // Fetch top songs and artists

    }

    private void getScreen() {
        View content = findViewById(android.R.id.content).getRootView();
        content.post(() -> {
            // Create a bitmap with the same size as the view
            Bitmap bitmap = Bitmap.createBitmap(content.getWidth(), content.getHeight(), Bitmap.Config.ARGB_8888);
            // Create a canvas to draw the view's content into the bitmap
            Canvas canvas = new Canvas(bitmap);
            content.draw(canvas);

            // Now you can use this bitmap to save or share
            saveAndShareImage(bitmap);
        });
    }

    private void saveAndShareImage(Bitmap bitmap) {
        try {
            String fileName = "layout_snapshot_" + System.currentTimeMillis() + ".png";
            Uri imageUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                try (OutputStream outputStream = getContentResolver().openOutputStream(imageUri)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } else {
                File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
                try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                }
                imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", imagePath);
            }

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://cs-2340-project-2-6ffe6.appspot.com");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            StorageReference storageRef = storage.getReference();
            StorageReference wrapRef = storageRef.child(currentUser.getUid() + "/" + fileName);
            UploadTask uploadTask = wrapRef.putFile(imageUri);

            shareImage(imageUri);
            Toast.makeText(this, "Snapshot saved and ready for sharing.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void shareImage(Uri imageUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Layout Snapshot"));
    }



    @Override
    public void onStart() {
        super.onStart();

        backBtn.setOnClickListener(view -> {
            finish();
        });
        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
                wrapped.getTopSongs(timeRange, new Wrapped.TopSongsCallback() {
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

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
                wrapped.getTimeListened(timeRange, new Wrapped.TimeListenedCallback() {
                    @Override
                    public void onSuccess(long timeListened) {
                        runOnUiThread(() -> {
                            if (timeListened >= 1) {
                                String text = timeListened + " Minutes Listened";
                                minsListened.setText(text);
                            } else {
                                Toast.makeText(Comb_wrap.this, "Must have listened to at least 1 song", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }
                });



            }


            public void onFailure(String errorMessage) {
                // Handle failure
            }
        });

        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
                wrapped.getTopArtists(timeRange, new Wrapped.TopArtistsCallback() {
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
        // Fetch top genre
        SpotifyAuthentication.refreshToken(new SpotifyAuthentication.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                Wrapped wrapped = new Wrapped(accessToken);
                wrapped.getTopGenres(timeRange, new Wrapped.TopGenresCallback() {
                    @Override
                    public void onSuccess(List<String> topGenres) {
                        // Update UI with top genre
                        runOnUiThread(() -> {
                            if (topGenres.size() >= 1) {
                                // Assuming you have a TextView named topGenreTextView
                                topGenre.setText(topGenres.get(0).substring(2, topGenres.size()));
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }
                });

            }
            public void onFailure(String errorMessage) {
                // Handle failure
            }
        });
    }



    // Helper method to load images into ImageViews
    private void loadImage(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }
}
