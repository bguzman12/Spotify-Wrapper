package com.example.cs2340project2.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.Homescreen;
import com.example.cs2340project2.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PreloginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialButton signup;
    private MaterialButton login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);
        mAuth = FirebaseAuth.getInstance();

        signup = findViewById(R.id.signup_btn);
        login = findViewById(R.id.login_btn);

        signup.setOnClickListener(view -> startActivity(new Intent(this, SignupActivity.class)));

        login.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, Homescreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            PreloginActivity.this.finish();
        }
    }
}
