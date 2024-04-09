package com.example.cs2340project2.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.Homescreen;
import com.example.cs2340project2.R;
import com.example.cs2340project2.SpotifyAuthentication;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Map<String, Object> userData;
    private MaterialToolbar toolbar;
    private TextInputLayout emailLayout;
    private TextInputEditText emailText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordText;
    private MaterialButton spotify;
    private MaterialButton login;
    private ActivityResultLauncher<Intent> spotifyAuthResLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userData = new HashMap<>();

        toolbar = findViewById(R.id.topAppBar_login);
        emailLayout = findViewById(R.id.email_layout);
        emailText = findViewById(R.id.email_input);
        passwordLayout = findViewById(R.id.password_layout);
        passwordText = findViewById(R.id.password_input);
        spotify = findViewById(R.id.spotify_btn);
        login = findViewById(R.id.login_btn);

        spotifyAuthResLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
                    if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                        userData.put("access_token", response.getAccessToken());
                        Timestamp currTime = Timestamp.now();
                        userData.put("expires", new Timestamp(currTime.getSeconds() + response.getExpiresIn(), currTime.getNanoseconds()));
                        if (validate()) {
                            enableButton();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.login_container), "Connect your Spotify account", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validate()) {
                    enableButton();
                } else {
                    disableButton();
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()) {
                        emailLayout.setError("Email address is not a valid email address");
                    } else {
                        emailLayout.setError("");
                    }
                }
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validate()) {
                    enableButton();
                } else {
                    disableButton();
                    if (passwordText.getText().length() == 0) {
                        passwordLayout.setError("Password cannot be empty");
                    } else {
                        passwordLayout.setError("");
                    }
                }
            }
        });

        spotify.setOnClickListener(view -> {
            spotifyAuthResLauncher.launch(new Intent(AuthorizationClient.createLoginActivityIntent(this, SpotifyAuthentication.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, false))));
        });

        login.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            db.collection("tokens").document(mAuth.getUid()).set(userData);
                            startActivity(new Intent(this, Homescreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            LoginActivity.this.finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(view, "Incorrect login credentials", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    private boolean validate() {
        return Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()
                && passwordText.getText().length() != 0
                && !userData.isEmpty();
    }

    private void enableButton() {
        login.setEnabled(true);
        login.setBackgroundColor(Color.WHITE);
        emailLayout.setError("");
        passwordLayout.setError("");
    }
    private void disableButton() {
        login.setEnabled(false);
        login.setBackgroundColor(Color.parseColor("#414141"));
    }
}
