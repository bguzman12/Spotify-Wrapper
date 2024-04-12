package com.example.cs2340project2;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationServiceConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpotifyAuthentication {
    private static final String CLIENT_ID = "fd02c77ee13e4dd2a8a5b88ecd17f2cc";
    private static final String REDIRECT_URI = "cs2340project2://auth";
    private static String[] scope = {"user-read-email"}; // TODO: change scope to required scope
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static Call mCall;

    public interface AccessTokenCallback {
        void onSuccess(String accessToken);
        void onFailure(String errorMessage);
    }
    private static final AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.spotify.com/authorize"),
            Uri.parse("https://accounts.spotify.com/api/token"));

    public static AuthorizationRequest getAuthenticationRequest(String type) {
        return new AuthorizationRequest.Builder(serviceConfig, CLIENT_ID, type, getRedirectUri())
                .setScopes(scope)
                .build();
    }

    public static void refreshToken(AccessTokenCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        AtomicReference<String> accessToken = new AtomicReference<>();
        DocumentReference docRef = db.collection("tokens").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (document.getTimestamp("expires").compareTo(Timestamp.now()) > 0) {
                        final RequestBody form = new FormBody.Builder()
                                .add("grant_type", "refresh_token")
                                .add("refresh_token", (String) document.get("refresh_token"))
                                .add("client_id", CLIENT_ID)
                                .build();
                        final Request request = new Request.Builder()
                                .url("https://accounts.spotify.com/api/token")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .post(form)
                                .build();

                        cancelCall();

                        mCall = mOkHttpClient.newCall(request);

                        mCall.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                callback.onFailure(e.getMessage());
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                try {
                                    final JSONObject jsonObject = new JSONObject(response.body().string());
                                    docRef.update("access_token", jsonObject.getString("access_token"));
                                    Timestamp currTime = Timestamp.now();
                                    docRef.update("expires", new Timestamp(currTime.getSeconds() + jsonObject.getInt("expires_in"), currTime.getNanoseconds()));
                                    docRef.update("refresh_token", jsonObject.get("refresh_token"));
                                    callback.onSuccess(jsonObject.getString("refresh_token"));
                                } catch (JSONException e) {
                                    callback.onFailure(e.getMessage());
                                }
                            }
                        });
                    } else {
                        callback.onSuccess(document.getString("access_token"));
                    }
                }
            }
        });
    }

    private static Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private static void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
