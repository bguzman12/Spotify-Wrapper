package com.example.cs2340project2.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.R;

import com.example.cs2340project2.utils.SignupViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class SignupActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private SignupViewModel signupViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.topAppBar_signup);
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.signup_fragment_container, SignupEmailFragment.class, null, "signupEmail")
                    .addToBackStack("signupEmail")
                    .commit();
        }

        toolbar.setNavigationOnClickListener(view -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                getSupportFragmentManager().popBackStack();
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });
    }
}
