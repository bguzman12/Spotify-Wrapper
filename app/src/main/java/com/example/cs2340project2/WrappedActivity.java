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


    public void fetchUserInfo() {
        try {
            // Get the access token
            String accessToken = getToken();

            // Create a URL object for the API endpoint
            URL url = new URL(API_URL + "?limit=50");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Read the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");

            // Extract song information from the JSON array
            List<SongInfo> songList = extractSongInfo(items);

        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    // Extracts song information from a JSON array and returns a list of SongInfo objects
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

    // Inner class to represent song information
    private static class SongInfo {
        private String name;
        private String artist;
        private long listeningTimeInSeconds;


        // Constructor
        public SongInfo(String name, long listeningTimeInSeconds) {
            this(name, null, listeningTimeInSeconds);
        }
        public SongInfo(String name, String artist, long listeningTimeInSeconds) {
            this.name = name;
            this.artist = artist;
            this.listeningTimeInSeconds = listeningTimeInSeconds;
        }

        // Getter for song name
        public String getName() {
            return name;
        }

        // Getter for listening time in seconds
        public long getListeningTimeInSeconds() {
            return listeningTimeInSeconds;
        }
    }

}
