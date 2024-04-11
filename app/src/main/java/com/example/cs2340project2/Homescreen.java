package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cs2340project2.ui.editlogin.EditLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    }

    public void newWrappedClicked(View view) {
        String accessToken = retrieveAccessTokenFromFirestore(); // Implement this method

        Intent intent = new Intent(this, Wrapped2.class);
        intent.putExtra("accessToken", accessToken);
        startActivity(intent);

    }
    private String retrieveAccessTokenFromFirestore() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("tokens").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String accessToken = documentSnapshot.getString("access_token");
                            // You can use the accessToken here or return it to the caller
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
        return null; // Return null if unable to retrieve access token
    }


    public void publicWrapsClicked(View view) {
        //TODO: go to public wraps ui (not currently created)
    }

    public void viewAccountClicked(View view) {
        startActivity(new Intent(this, MyAccount.class));
    }
}