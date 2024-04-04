package com.example.cs2340project2;

import com.example.cs2340project2.ui.login.Firebase;
import com.example.cs2340project2.ui.login.SignupTabFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

            long totalListeningTime = getTotalListeningTime(items);
            // Update UI with totalListeningTime, e.g., display it in a TextView
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, e.g., display an error message to the user
        }
    }


    private static long getTotalListeningTime(JSONArray items) throws JSONException {
        long totalListeningTime = 0;
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject track = item.getJSONObject("track");
            long durationMs = track.getLong("duration_ms");
            totalListeningTime += durationMs;
        }
        return totalListeningTime / 1000; // Convert to seconds
    }
}
