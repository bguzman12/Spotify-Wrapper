package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    }

    public void newWrappedClicked(View view) {
        //TODO: change to go to wrapped1 first
        startActivity(new Intent(this, Wrapped1.class));

    }

    public void pastWrapsClicked(View view) {
        //TODO: go to public wraps ui (not currently created)
        startActivity(new Intent(this, PastWraps.class));
    }

    public void viewAccountClicked(View view) {
        startActivity(new Intent(this, MyAccount.class));
    }
}