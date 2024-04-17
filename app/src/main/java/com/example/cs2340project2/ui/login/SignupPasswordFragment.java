package com.example.cs2340project2.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.R;
import com.example.cs2340project2.utils.SignupViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupPasswordFragment extends Fragment {
    private SignupViewModel signupViewModel;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordText;
    private MaterialButton next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        passwordLayout = requireView().findViewById(R.id.password_layout);
        passwordText = requireView().findViewById(R.id.password_input);
        next = requireView().findViewById(R.id.next_btn);
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (validate()) {
            enableButton();
        }

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
                    if (passwordText.getText().length() < 6) {
                        passwordLayout.setError("Password must be at least 6 characters");
                    } else {
                        passwordLayout.setError("");
                    }
                }
            }
        });

        next.setOnClickListener(view -> {
            signupViewModel.setPassword(passwordText.getText().toString());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.signup_fragment_container, SignupSpotifyFragment.class, null, "signupSpotify")
                    .addToBackStack("signupSpotify")
                    .commit();
        });
    }

    private boolean validate() {
        return passwordText.getText().length() >= 6;
    }

    private void enableButton() {
        next.setEnabled(true);
        next.setBackgroundColor(Color.WHITE);
        passwordLayout.setError("");
    }

    private void disableButton() {
        next.setEnabled(false);
        next.setBackgroundColor(Color.parseColor("#414141"));
    }
}
