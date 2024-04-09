package com.example.cs2340project2.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import com.example.cs2340project2.SpotifyAuthentication;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.HashMap;
import java.util.Map;

public class SignupSpotifyFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Map<String, Object> userData;
    private SignupViewModel signupViewModel;
    private MaterialButton spotify;
    private MaterialButton signup;
    private ActivityResultLauncher<Intent> spotifyAuthResLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_spotify, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userData = new HashMap<>();

        spotifyAuthResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
                    if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                        userData.put("access_token", response.getAccessToken());
                        Timestamp currTime = Timestamp.now();
                        userData.put("expires", new Timestamp(currTime.getSeconds() + response.getExpiresIn(), currTime.getNanoseconds()));
                        enableButton();
                    } else {
                        Snackbar.make(getView(), "Connect your Spotify account", Snackbar.LENGTH_SHORT).show();
                    }
                });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spotify = getView().findViewById(R.id.spotify_btn);
        signup = getView().findViewById(R.id.signup_btn);
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        spotify.setOnClickListener(view -> {
            spotifyAuthResLauncher.launch(new Intent(AuthorizationClient.createLoginActivityIntent(getActivity(), SpotifyAuthentication.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, false))));
        });

        signup.setOnClickListener(view -> {
            mAuth.createUserWithEmailAndPassword(signupViewModel.getEmail(), signupViewModel.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            db.collection("tokens").document(mAuth.getUid()).set(userData);
                            startActivity(new Intent(getContext(), Homescreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            getActivity().finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Snackbar.make(view, "User with the same email already exists", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(view, "Failed to create account", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    private void enableButton() {
        signup.setEnabled(true);
        signup.setBackgroundColor(Color.WHITE);
    }
}
