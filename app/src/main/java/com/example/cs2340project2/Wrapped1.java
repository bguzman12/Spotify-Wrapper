package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.Random;


public class Wrapped1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped1);

        View myBackgroundView = findViewById(R.id.myBackgroundView);
        myBackgroundView.setBackgroundResource(getRandomDrawable1());
    }

    private int getRandomDrawable1() {
        int[] drawables = new int[] {R.drawable.topsongs1, R.drawable.topsongs2, R.drawable.topsongs3, R.drawable.topsongs4};
        int randomIndex = new Random().nextInt(drawables.length);
        return drawables[randomIndex];
    }
}
