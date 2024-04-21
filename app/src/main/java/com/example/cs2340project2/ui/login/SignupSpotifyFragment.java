package com.example.cs2340project2.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.Homescreen;
import com.example.cs2340project2.R;
import com.example.cs2340project2.utils.SignupViewModel;
import com.example.cs2340project2.utils.SpotifyAuthentication;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.ResponseTypeValues;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupSpotifyFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Map<String, Object> userData;
    private SignupViewModel signupViewModel;
    private MaterialButton spotify;
    private MaterialButton signup;
    private ActivityResultLauncher<Intent> authResLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_spotify, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userData = new HashMap<>();

        authResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result != null) {
                        final AuthorizationResponse response = AuthorizationResponse.fromIntent(result.getData());
                        if (response != null) {
                            AuthorizationService authService = new AuthorizationService(requireActivity());
                            authService.performTokenRequest(response.createTokenExchangeRequest(),
                                    (resp, ex) -> {
                                        if (resp != null && resp.accessTokenExpirationTime != null) {
                                            userData.put("access_token", resp.accessToken);
                                            userData.put("expires", new Timestamp(new Date(resp.accessTokenExpirationTime)));
                                            userData.put("refresh_token", resp.refreshToken);
                                            userData.put("posts", 0);
                                            userData.put("public_posts", 0);
                                            enableButton();
                                            Snackbar.make(requireView(), "Spotify account connected", Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Snackbar.make(requireView(), "Connect your Spotify account", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spotify = requireView().findViewById(R.id.spotify_btn);
        signup = requireView().findViewById(R.id.signup_btn);
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        AuthorizationService authService = new AuthorizationService(requireActivity());
        authResLauncher.launch(new Intent(authService.getAuthorizationRequestIntent(SpotifyAuthentication.getAuthenticationRequest(ResponseTypeValues.CODE))));

        spotify.setOnClickListener(v -> authResLauncher.launch(new Intent(authService.getAuthorizationRequestIntent(SpotifyAuthentication.getAuthenticationRequest(ResponseTypeValues.CODE)))));

        signup.setOnClickListener(v ->
                mAuth.createUserWithEmailAndPassword(signupViewModel.getEmail(), signupViewModel.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            db.collection("tokens").document(mAuth.getUid()).set(userData);
                            db.collection("pastwraps").document(mAuth.getUid()).set(new HashMap<>());
                            startActivity(new Intent(getContext(), Homescreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            requireActivity().finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Snackbar.make(v, "User with the same email already exists", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(v, "Failed to create account", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }));
    }

    private void enableButton() {
        requireActivity().runOnUiThread(() -> {
            signup.setEnabled(true);
            signup.setBackgroundColor(Color.WHITE);
        });
    }
}
