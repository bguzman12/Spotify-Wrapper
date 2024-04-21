package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.ui.editlogin.EditLoginActivity;

public class MyAccount extends AppCompatActivity {
    protected ImageButton homeBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        homeBtn = findViewById(R.id.imageButton);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount.this, Homescreen.class);
                startActivity(intent);
            }
        });
    }

    public void editLoginClicked(View view) {
        startActivity(new Intent(this, EditLoginActivity.class));
    }

    public void prevWrapsClicked(View view) {
    }

    public void viewPostsClicked(View view) {
    }
}