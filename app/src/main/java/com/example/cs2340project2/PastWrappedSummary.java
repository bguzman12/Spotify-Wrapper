package com.example.cs2340project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class PastWrappedSummary extends AppCompatActivity {
    Button homeButton;
    RelativeLayout relativeLayout;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_wrapped);
        // TODO : on create, the relative layout needs to be saved to user profile, but currently private
        relativeLayout = findViewById(R.id.summary_img);
        imageView = findViewById(R.id.image_view);


    }




    public void backButtonClicked(View view) {
        Intent intent = new Intent(this, Homescreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}