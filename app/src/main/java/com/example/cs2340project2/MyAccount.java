package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cs2340project2.ui.editlogin.EditLoginActivity;

public class MyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
    }

    public void editLoginClicked(View view) {
        startActivity(new Intent(this, EditLoginActivity.class));
    }

    public void prevWrapsClicked(View view) {
    }

    public void viewPostsClicked(View view) {
    }
}