package com.example.cs2340project2;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WrappedActivity extends Activity {

    private static final String API_URL = "https://api.spotify.com/v1/me/player/recently-played";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String accessToken; // Store access token here

    public enum TimeRange {
        WEEK,
        MONTH,
        YEAR
    }

    public WrappedActivity() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Method to set the access token
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void fetchUserInfoAsync(TimeRange timeRange, FetchUserInfoListener listener) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<SongInfo> songList = doInBackground(timeRange);
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onUserInfoFetched(songList);
            });
        });
    }

    private List<SongInfo> doInBackground(TimeRange timeRange) {
        List<SongInfo> songList = new ArrayList<>();
        try {
            String formattedStartTime, formattedEndTime;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Calendar calendar = Calendar.getInstance();

            switch (timeRange) {
                case WEEK:
                    calendar.add(Calendar.DAY_OF_YEAR, -7);
                    break;
                case MONTH:
                    calendar.add(Calendar.MONTH, -1);
                    break;
                case YEAR:
                    calendar.add(Calendar.YEAR, -1);
                    break;
            }

            formattedStartTime = sdf.format(calendar.getTime());
            formattedEndTime = sdf.format(new Date());

            // Use the access token obtained during authentication
            if (accessToken == null) {
                throw new IllegalStateException("Access token is null. Make sure to set it before making the request.");
            }

            URL url = new URL(API_URL + "?limit=50&start_time=" + formattedStartTime + "&end_time=" + formattedEndTime);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");

            songList = extractSongInfo(items);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("your call failed :/");
        }
        return songList;
    }

    private List<SongInfo> extractSongInfo(JSONArray items) throws JSONException {
        List<SongInfo> songList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject track = item.getJSONObject("track");
            String songName = track.getString("name");

            JSONArray artists = track.getJSONArray("artists");
            JSONObject artist = artists.getJSONObject(0);
            String artistName = artist.getString("name");

            long durationMs = track.getLong("duration_ms");
            long listeningTimeInSeconds = durationMs / 1000;
            songList.add(new SongInfo(songName, artistName, listeningTimeInSeconds));
        }
        return songList;
    }

    public interface FetchUserInfoListener {
        void onUserInfoFetched(List<SongInfo> songList);
    }

    static class SongInfo {
        private String name;
        private String artist;
        private long listeningTimeInSeconds;

        public SongInfo(String name, String artist, long listeningTimeInSeconds) {
            this.name = name;
            this.artist = artist;
            this.listeningTimeInSeconds = listeningTimeInSeconds;
        }

        public String getName() {
            return name;
        }

        public long getListeningTimeInSeconds() {
            return listeningTimeInSeconds;
        }
    }
}
