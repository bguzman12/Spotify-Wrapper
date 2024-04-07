package com.example.cs2340project2.ui.editlogin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.cs2340project2.R;

import com.example.cs2340project2.ui.login.LaunchActivity;
import com.example.cs2340project2.ui.login.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialToolbar toolbar;
    private TextView email;
    private Button editEmail;
    private Button editPassword;
    private ConstraintLayout deleteAccount;
    private Button signOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlogin);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.topAppBar_editLogin);
        email = findViewById(R.id.editEmail_email);
        editEmail = findViewById(R.id.editEmail_btn);
        editPassword = findViewById(R.id.editPassword_btn);
        deleteAccount = findViewById(R.id.deleteAccount_layout);
        signOut = findViewById(R.id.signout_btn);
    }

    @Override
    public void onStart() {
        super.onStart();

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        email.setText(currentUser.getEmail());
        editEmail.setOnClickListener(view -> {
            startActivity(new Intent(this, EditEmailActivity.class));
        });
        editPassword.setOnClickListener(view -> {
            startActivity(new Intent(this, EditPasswordActivity.class));
        });
        deleteAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, DeleteAccountActivity.class));
        });
        signOut.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this, R.style.WarningAlertDialogTheme)
                    .setTitle("Warning")
                    .setIcon(R.drawable.warning_icon)
                    .setMessage("Signing out will not sign you out of Spotify. To change Spotify accounts, sign out of the Spotify app and the Spotify website on your default browser.")
                    .setPositiveButton("Continue", (dialog, which) -> {
                        mAuth.signOut();
                        startActivity(new Intent(this, LaunchActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        EditLoginActivity.this.finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
