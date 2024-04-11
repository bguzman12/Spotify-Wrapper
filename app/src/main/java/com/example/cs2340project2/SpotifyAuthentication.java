package com.example.cs2340project2;

import android.net.Uri;

import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class SpotifyAuthentication {
    private static final String CLIENT_ID = "fd02c77ee13e4dd2a8a5b88ecd17f2cc";
    private static final String REDIRECT_URI = "cs2340project2://auth";
    private static String[] scope = {"user-read-email", "user-top-read", "user-read-recently-played"}; // TODO: change scope to required scope
    private static String accessToken; // Store access token here

    public static AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type, boolean showDialog) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(showDialog)
                .setScopes(scope)
                .setCampaign("CS2340Project2") // TODO: change to name of app
                .build();
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    private static Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }
}
