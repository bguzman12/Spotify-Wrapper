package com.example.cs2340project2.ui.editlogin;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class EditPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialToolbar toolbar;
    private Button saveEdits;
    private TextInputLayout oldPasswordLayout;
    private TextInputEditText oldPasswordText;
    private TextInputLayout newPasswordLayout;
    private TextInputEditText newPasswordText;
    private TextInputLayout confirmPasswordLayout;
    private TextInputEditText confirmPasswordText;
    private FirebaseUser currentUser;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.topAppBar_editPassword);
        saveEdits = findViewById(R.id.saveEditPassword_btn);
        oldPasswordLayout = findViewById(R.id.old_password_layout);
        oldPasswordText = findViewById(R.id.old_password_input);
        newPasswordLayout = findViewById(R.id.new_password_layout);
        newPasswordText = findViewById(R.id.new_password_input);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);
        confirmPasswordText = findViewById(R.id.confirm_password_input);

        currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();

        toolbar.setNavigationOnClickListener(view -> finish());

        oldPasswordText.addTextChangedListener(new TextWatcher() {
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
                    if (oldPasswordText.getText().length() == 0) {
                        runOnUiThread(() -> oldPasswordLayout.setError("Password cannot be empty"));
                    } else {
                        runOnUiThread(() -> oldPasswordLayout.setError(""));
                    }
                }
            }
        });
        newPasswordText.addTextChangedListener(new TextWatcher() {
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
                    if (newPasswordText.getText().length() < 6) {
                        runOnUiThread(() -> newPasswordLayout.setError("Password must be at least 6 characters"));
                    } else {
                        runOnUiThread(() -> newPasswordLayout.setError(""));
                    }
                }
            }
        });
        confirmPasswordText.addTextChangedListener(new TextWatcher() {
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
                    if (!newPasswordText.getText().toString().equals(confirmPasswordText.getText().toString())) {
                        runOnUiThread(() -> confirmPasswordLayout.setError("Passwords do not match"));
                    } else {
                        runOnUiThread(() -> confirmPasswordLayout.setError(""));
                    }
                }
            }
        });
        saveEdits.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            currentUser.reauthenticate(EmailAuthProvider.getCredential(email, oldPasswordText.getText().toString()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(newPasswordText.getText().toString())
                                    .addOnCompleteListener(task2 -> finish());
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                runOnUiThread(() -> {
                                    oldPasswordLayout.setError("Incorrect password");
                                    saveEdits.setEnabled(false);
                                    saveEdits.setTextColor(Color.parseColor("#b8b8b8"));
                                });
                            }
                        }
                    });
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();
    }

    private boolean validate() {
        return oldPasswordText.getText().length() != 0
                && newPasswordText.getText().toString().equals(confirmPasswordText.getText().toString())
                && newPasswordText.getText().length() >= 6;
    }

    private void enableButton() {
        runOnUiThread(() -> {
            saveEdits.setEnabled(true);
            saveEdits.setTextColor(Color.WHITE);
            oldPasswordLayout.setError("");
            newPasswordLayout.setError("");
            confirmPasswordLayout.setError("");
        });
    }

    private void disableButton() {
        runOnUiThread(() -> {
            saveEdits.setEnabled(false);
            saveEdits.setTextColor(Color.parseColor("#b8b8b8"));
        });
    }
}
