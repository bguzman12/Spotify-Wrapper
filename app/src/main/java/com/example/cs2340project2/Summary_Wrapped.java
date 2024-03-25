package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Summary_Wrapped extends AppCompatActivity {
    Button shareButton;
    Button homeButton;
    Button postButton;
    RelativeLayout relativeLayout;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_wrapped);
        // TODO : on create, the relative layout needs to be saved to user profile, but currently private

        shareButton = findViewById(R.id.button_share);
        homeButton = findViewById(R.id.button_back);
        postButton = findViewById(R.id.button_post);
        relativeLayout = findViewById(R.id.summary_img);
        imageView = findViewById(R.id.image_view);

    }




    public void backButtonClicked(View view) {
        Intent intent = new Intent(this, Homescreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void shareButtonClicked(View view) {
        // TODO : logic for this... however we want to implement it
        // TODO : move this over to publicwraps.java
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        relativeLayout.draw(canvas);

        imageView.setImageBitmap(bitmap);
    }

    public void postButtonClicked(View view) {
        // TODO : make relative layout img public (or add to separate public wraps database?)
        // TODO : needs to go to public wraps page
    }
}