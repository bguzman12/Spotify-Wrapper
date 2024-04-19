package com.example.cs2340project2.ui.editlogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.R;
import com.example.cs2340project2.ui.login.PreloginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class EditEmailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialToolbar toolbar;
    private Button saveEdits;
    private TextInputLayout newEmailLayout;
    private TextInputEditText newEmailText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordText;
    private FirebaseUser currentUser;
    private String originalEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editemail);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.topAppBar_editEmail);
        saveEdits = findViewById(R.id.saveEditEmail_btn);
        newEmailLayout = findViewById(R.id.new_email_layout);
        newEmailText = findViewById(R.id.new_email_input);
        passwordLayout = findViewById(R.id.password_layout);
        passwordText = findViewById(R.id.password_input);

        toolbar.setNavigationOnClickListener(view -> finish());

        currentUser = mAuth.getCurrentUser();
        originalEmail = currentUser.getEmail();
        runOnUiThread(() -> {
            newEmailLayout.setPlaceholderText(originalEmail);
            newEmailText.setText(originalEmail);
        });
        newEmailText.addTextChangedListener(new TextWatcher() {
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
                    if (!Patterns.EMAIL_ADDRESS.matcher(newEmailText.getText().toString()).matches()) {
                        runOnUiThread(() -> newEmailLayout.setError("Email address is not a valid email address"));
                    } else {
                        runOnUiThread(() -> newEmailLayout.setError(""));
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
                        runOnUiThread(() -> passwordLayout.setError("Password cannot be empty"));
                    } else {
                        runOnUiThread(() -> passwordLayout.setError(""));
                    }
                }
            }
        });
        saveEdits.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            currentUser.reauthenticate(EmailAuthProvider.getCredential(originalEmail, passwordText.getText().toString()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser.verifyBeforeUpdateEmail(newEmailText.getText().toString().trim())
                                    .addOnCompleteListener(task2 -> {
                                        Toast.makeText(this, "Verify email address to complete changes.", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                        startActivity(new Intent(this, PreloginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        EditEmailActivity.this.finish();
                                    });
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                passwordLayout.setError("Incorrect password");
                                disableButton();
                            }
                        }
                    });
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();
        originalEmail = currentUser.getEmail();
        runOnUiThread(() -> {
            newEmailLayout.setPlaceholderText(originalEmail);
            newEmailText.setText(originalEmail);
        });
    }

    private boolean validate() {
        return !newEmailText.getText().toString().trim().equals(originalEmail)
                && Patterns.EMAIL_ADDRESS.matcher(newEmailText.getText().toString()).matches()
                && passwordText.getText().length() != 0;
    }

    private void enableButton() {
        runOnUiThread(() -> {
            saveEdits.setEnabled(true);
            saveEdits.setTextColor(Color.WHITE);
            newEmailLayout.setError("");
            passwordLayout.setError("");
        });
    }

    private void disableButton() {
        runOnUiThread(() -> {
            saveEdits.setEnabled(false);
            saveEdits.setTextColor(Color.parseColor("#b8b8b8"));
        });
    }
}
