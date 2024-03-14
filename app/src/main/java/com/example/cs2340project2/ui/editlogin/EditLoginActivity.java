package com.example.cs2340project2.ui.editlogin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.R;
import com.example.cs2340project2.ui.login.LoginActivity;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;


public class EditLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button saveEdits;
    private Button deleteAccount;
    private Button signOut;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlogin);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editEmail_ET);
        password = findViewById(R.id.editPassword_ET);
        saveEdits = findViewById(R.id.saveEdits_btn);
        deleteAccount = findViewById(R.id.deleteAcc_btn);
        signOut = findViewById(R.id.signOut_btn);
    }

    // TODO: Reauthentication needed for all changes and deletion of account
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            signOut.setOnClickListener(view -> {
                mAuth.signOut();
                Toast.makeText(getBaseContext(), "Signed out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            });
            deleteAccount.setOnClickListener(view -> {
                new AlertDialog.Builder(getBaseContext())
                        .setTitle("Delete account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("yes", (dialog, which) -> currentUser.delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getBaseContext(), "Account deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                }))
                        .setNegativeButton("no", null)
                        .show();
            });
            saveEdits.setOnClickListener(view -> {
                boolean changed = false;
                if (!email.getText().toString().trim().isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        Toast.makeText(getBaseContext(), "Email address is invalid.", Toast.LENGTH_SHORT).show();
                    } else {
                        currentUser.verifyBeforeUpdateEmail(email.getText().toString().trim())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("EditLogin Email Address", "User email updated to " + currentUser.getEmail());
                                    } else {
                                        Log.d("EditLogin Email Address", "Update failed" + task.getException() + "; curr email: " + currentUser.getEmail());
                                    }
                                })
                        ;
                        changed = true;
                    }
                }
                if (password.getText().toString().trim().length() >= 6) {
                    currentUser.updatePassword(password.getText().toString().trim());
                    changed = true;
                } else if (!password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                    changed = true;
                }
                if (!changed) {
                    Toast.makeText(getBaseContext(), "Nothing was changed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "The edits were saved.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            signOut.setOnClickListener(view -> {
                Toast.makeText(getBaseContext(), "Not signed in!", Toast.LENGTH_SHORT).show();
            });
            deleteAccount.setOnClickListener(view -> {
                Toast.makeText(getBaseContext(), "Not signed in!", Toast.LENGTH_SHORT).show();
            });
            saveEdits.setOnClickListener(view -> {
                Toast.makeText(getBaseContext(), "Not signed in!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
