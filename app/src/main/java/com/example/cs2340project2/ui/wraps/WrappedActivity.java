package com.example.cs2340project2.ui.wraps;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.R;
import com.example.cs2340project2.utils.ArtistInfo;
import com.example.cs2340project2.utils.SongInfo;
import com.example.cs2340project2.utils.WrapViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class WrappedActivity extends AppCompatActivity {
    private FloatingActionButton pause;
    private FloatingActionButton mute;
    private FloatingActionButton close;
    private FloatingActionButton share;
    private boolean muted = false;
    private boolean paused = false;
    private CountDownTimer cd;
    private LinearProgressIndicator progress;
    private MediaPlayer mp;
    private final long milliTimer = 5000L;
    private long currMilli = 5000L;
    private WrapViewModel wrapViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

        mp = new MediaPlayer();
        mp.setLooping(true);

        Bundle extras = getIntent().getExtras();

        pause = findViewById(R.id.pause_fab);
        mute = findViewById(R.id.mute_fab);
        close = findViewById(R.id.close_fab);
        share = findViewById(R.id.share_fab);
        progress = findViewById(R.id.wrapped_progress);

        wrapViewModel = new ViewModelProvider(this).get(WrapViewModel.class);

        wrapViewModel.setTimeRange(extras.getString("timeRange"));
        wrapViewModel.setTopSongs(extras.getSerializable("topSongs", (Class<? extends ArrayList<SongInfo>>) new ArrayList<>().getClass()));
        wrapViewModel.setTopArtists(extras.getSerializable("topArtists", (Class<? extends ArrayList<ArtistInfo>>) new ArrayList<>().getClass()));
        wrapViewModel.setGeneratedDate(extras.getString("generatedDate"));
        wrapViewModel.setPaused(paused);
        wrapViewModel.setMediaPlayer(mp);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.wrapped_fragment_container, WrappedStartFragment.class, null, "welcome")
                .addToBackStack("welcome")
                .commit();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    getSupportFragmentManager().popBackStack();
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                runOnUiThread(() -> {
                    share.hide();
                    progress.show();
                    pause.show();
                    mute.show();
                });
                cd.cancel();
                resetProgress();
                cd = nextFragTimer(milliTimer);
                if (!paused) {
                    cd.start();
                }
            }
        });

        pause.setOnClickListener(view -> {
            if (!paused) {
                mp.pause();
                cd.cancel();
                runOnUiThread(() -> pause.setImageResource(R.drawable.play_icon));
            } else {
                mp.start();
                cd = nextFragTimer(currMilli);
                cd.start();
                runOnUiThread(() -> pause.setImageResource(R.drawable.pause_icon));
            }
            paused = !paused;
            wrapViewModel.setPaused(paused);
        });

        mute.setOnClickListener(view -> {
            if (!muted) {
                mp.setVolume(0,0);
                runOnUiThread(() -> mute.setImageResource(R.drawable.volume_off_icon));

            } else {
                mp.setVolume(1, 1);
                runOnUiThread(() -> mute.setImageResource(R.drawable.volume_max_icon));
            }
            muted = !muted;
        });

        close.setOnClickListener(view -> {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            cd.cancel();
            finish();
        });

        share.setOnClickListener(view -> {
            Bitmap savedBitmap = getScreen();
            Uri filepath = saveImage(savedBitmap);
            if (filepath != null) {
                shareImage(filepath);
            }
        });

        cd = nextFragTimer(milliTimer);
    }

    @Override
    public void onStart() {
        super.onStart();

        resetProgress();
        cd.start();
    }

    private Bitmap getScreen() {
        View screenView = findViewById(R.id.wrapped_summary_container);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getWidth(), screenView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        screenView.draw(canvas);
        return bitmap;
    }

    private Uri saveImage(Bitmap bitmap) {
        try {
            String filename = "wrapped_" + System.currentTimeMillis() + ".png";
            Uri imageURI;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                imageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                try (OutputStream outputStream = getContentResolver().openOutputStream(imageURI)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } else {
                File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
                try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                }
                imageURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", imagePath);
            }

            return imageURI;
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.wrapped_container), "Failed to save file", Snackbar.LENGTH_SHORT).show();
            return null;
        }
    }

    private void shareImage(Uri imagepath) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imagepath);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Wrap"));
    }

    private void resetProgress() {
        runOnUiThread(() -> {
            progress.setProgressCompat(0, false);
            progress.setMax((int) (milliTimer));
        });
        currMilli = 5000L;
    }

    private CountDownTimer nextFragTimer(long milliseconds) {
        return new CountDownTimer(milliseconds, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int currProgress = (int) (progress.getMax() - millisUntilFinished);
                runOnUiThread(() -> progress.setProgressCompat(currProgress, true));
                currMilli = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                resetProgress();
                navigate();
                cd = nextFragTimer(milliTimer);
                cd.start();
            }
        };
    }

    private void navigate() {
        String currFrag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        switch (currFrag) {
            case "welcome":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.wrapped_fragment_container, WrappedTopSingleFragment.class, null, "topSong")
                        .addToBackStack("topSong")
                        .commit();
                break;
            case "topSong":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.wrapped_fragment_container, WrappedTopSingleFragment.class, null, "topArtist")
                        .addToBackStack("topArtist")
                        .commit();
                break;
            case "topArtist":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.wrapped_fragment_container, WrappedTopListFragment.class, null, "topSongs")
                        .addToBackStack("topSongs")
                        .commit();
                break;
            case "topSongs":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.wrapped_fragment_container, WrappedTopListFragment.class, null, "topArtists")
                        .addToBackStack("topArtists")
                        .commit();
                break;
            case "topArtists":
                runOnUiThread(() -> {
                    mute.hide();
                    pause.hide();
                    progress.hide();
                    share.show();
                    mp.reset();
                });
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.wrapped_fragment_container, WrappedSummaryFragment.class, null, "summary")
                        .addToBackStack("summary")
                        .commit();
                break;
            default:
                break;
        }
    }
}
