package com.example.cs2340project2;

import com.example.cs2340project2.ui.login.Firebase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WrappedActivity extends Firebase {

    private static final String API_URL = "https://api.spotify.com/v1/me/player/recently-played";
    // private static final String ACCESS_TOKEN = token;

    // Inside WrappedActivity class
    public void fetchUserInfo() {
        try {
            String accessToken = getToken();
            URL url = new URL(API_URL + "?limit=50");
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

            List<SongInfo> songList = extractSongInfo(items);


        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, e.g., display an error message to the user
        }
    }


    private static List<SongInfo> extractSongInfo(JSONArray items) throws JSONException {
        List<SongInfo> songList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject track = item.getJSONObject("track");
            String songName = track.getString("name");
            long durationMs = track.getLong("duration_ms");
            long listeningTimeInSeconds = durationMs / 1000;
            songList.add(new SongInfo(songName, listeningTimeInSeconds));
        }
        return songList;
    }


    private static class SongInfo {
        private String name;
        private long listeningTimeInSeconds;

        public SongInfo(String name, long listeningTimeInSeconds) {
            this.name = name;
            this.listeningTimeInSeconds = listeningTimeInSeconds;
        }

        // Getter methods
        public String getName() {
            return name;
        }

        public long getListeningTimeInSeconds() {
            return listeningTimeInSeconds;
        }
    }
}
