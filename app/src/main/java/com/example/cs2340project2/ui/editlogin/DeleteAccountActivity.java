package com.example.cs2340project2.ui.editlogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.R;
import com.example.cs2340project2.ui.login.PreloginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DeleteAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MaterialToolbar toolbar;
    private Button deleteAccount;
    private TextInputLayout emailLayout;
    private TextInputEditText emailText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordText;
    private String originalEmail;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteaccount);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.topAppBar_deleteAccount);
        deleteAccount = findViewById(R.id.deleteAccount_btn);
        emailLayout = findViewById(R.id.email_layout);
        emailText = findViewById(R.id.email_input);
        passwordLayout = findViewById(R.id.password_layout);
        passwordText = findViewById(R.id.password_input);

        currentUser = mAuth.getCurrentUser();
        originalEmail = currentUser.getEmail();

        toolbar.setNavigationOnClickListener(view -> finish());

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
                        runOnUiThread(() -> emailLayout.setError("Email address is not a valid email address"));
                    } else if (!emailText.getText().toString().trim().equals(originalEmail)) {
                        runOnUiThread(() -> emailLayout.setError("Incorrect email address"));
                    } else {
                        runOnUiThread(() -> emailLayout.setError(""));
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
        deleteAccount.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            currentUser.reauthenticate(EmailAuthProvider.getCredential(originalEmail, passwordText.getText().toString()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AlertDialog dialog = new MaterialAlertDialogBuilder(this, R.style.WarningAlertDialogTheme)
                                    .setTitle("Are you sure?")
                                    .setIcon(R.drawable.warning_icon)
                                    .setMessage("Deleting your account will erase all your Spotify Wraps.")
                                    .setPositiveButton("Yes", (dialog2, which) -> {
                                        db.collection("tokens").document(currentUser.getUid()).delete()
                                                .addOnSuccessListener(t -> db.collection("pastwraps").document(currentUser.getUid()).delete()
                                                        .addOnSuccessListener(t2 -> currentUser.delete()
                                                                .addOnSuccessListener(t3 -> {
                                                                    mAuth.signOut();
                                                                    startActivity(new Intent(this, PreloginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                                    DeleteAccountActivity.this.finish();
                                                                })));
                                    })
                                    .setNegativeButton("No", (dialog2, which) -> finish())
                                    .create();
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                private static final int AUTO_DISMISS_MILLIS = 5000;
                                @Override
                                public void onShow(final DialogInterface dialog) {
                                    final Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                                    final CharSequence negativeButtonText = negativeButton.getText();
                                    new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            negativeButton.setText(String.format(
                                                    Locale.getDefault(), "%s (%d)",
                                                    negativeButtonText,
                                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                                            ));
                                        }
                                        @Override
                                        public void onFinish() {
                                            if (((AlertDialog) dialog).isShowing()) {
                                                dialog.dismiss();
                                                Snackbar.make(view, "Failed to confirm account deletion on time.", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }.start();
                                }
                            });
                            dialog.show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                runOnUiThread(() -> {
                                    passwordLayout.setError("Incorrect password");
                                    deleteAccount.setEnabled(false);
                                    deleteAccount.setTextColor(Color.parseColor("#b8b8b8"));
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
        originalEmail = currentUser.getEmail();
    }

    private boolean validate() {
        return emailText.getText().toString().trim().equals(originalEmail)
                && Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()
                && passwordText.getText().length() != 0;
    }

    private void enableButton() {
        runOnUiThread(() -> {
            deleteAccount.setEnabled(true);
            deleteAccount.setTextColor(Color.WHITE);
            emailLayout.setError("");
            passwordLayout.setError("");
        });
    }

    private void disableButton() {
        runOnUiThread(() -> {
            deleteAccount.setEnabled(false);
            deleteAccount.setTextColor(Color.parseColor("#b8b8b8"));
        });
    }
}
