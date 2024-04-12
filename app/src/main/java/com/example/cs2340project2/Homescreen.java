package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cs2340project2.ui.editlogin.EditLoginActivity;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    }

    public void newWrappedClicked(View view) {
        //TODO: go to new wrapped ui (not currently created)
    }

    public void publicWrapsClicked(View view) {
        //TODO: go to public wraps ui (not currently created)
    }

    public void viewAccountClicked(View view) {
        startActivity(new Intent(this, MyAccount.class));
    }
}