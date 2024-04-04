package com.example.cs2340project2;

import com.example.cs2340project2.ui.login.Firebase;


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

public class WrappedActivity extends Firebase {

    private static final String API_URL = "https://api.spotify.com/v1/me/player/recently-played";
    // private static final String ACCESS_TOKEN = token;


    //TODO : implement logic elsewhere to choose start and end range
    public void fetchUserInfo(String startTime, String endTime) {
        try {
            String formattedStartTime, formattedEndTime;

            //if null, set time range to YTD
            if (startTime == null && endTime == null) {
                // Set startTime to the first day of the year
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                formattedStartTime = sdf.format(calendar.getTime());

                // Set endTime to the current date and time
                formattedEndTime = sdf.format(new Date());
            } else {
                // Format the start and end time to match the API's expected format, can move this elsewhere later
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                formattedStartTime = sdf.format(sdf.parse(startTime));
                formattedEndTime = sdf.format(sdf.parse(endTime));
            }

            // Get the access token
            String accessToken = getToken();

            // Create a URL object for the API endpoint
            URL url = new URL(API_URL + "?limit=50&start_time=" + formattedStartTime + "&end_time=" + formattedEndTime);

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
            System.out.print("your call failed :/");
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

        public String getName() {
            return name;
        }

        public long getListeningTimeInSeconds() {
            return listeningTimeInSeconds;
        }
    }
}
