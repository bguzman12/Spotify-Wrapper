package com.example.cs2340project2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.Random;


public class Wrapped2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped2);

        //View myBackgroundView = findViewById(R.id.myBackgroundView);
        //myBackgroundView.setBackgroundResource(getRandomDrawable());
    }

    /*
    private int getRandomDrawable() {
        int[] drawables = new int[] {R.drawable.wrapped1purple, R.drawable.wrapped2orange, R.drawable.wrapped3blue, R.drawable.wrapped4yellow};
        int randomIndex = new Random().nextInt(drawables.length);
        return drawables[randomIndex];
    }

     */
}
