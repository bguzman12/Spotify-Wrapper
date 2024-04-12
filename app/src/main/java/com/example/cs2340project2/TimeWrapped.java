package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TimeWrapped extends AppCompatActivity {
    private Button pastMonth;
    private Button past6Months;
    private Button pastYear;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_wrapped);

        pastMonth = findViewById(R.id.past_month_btn);
        past6Months = findViewById(R.id.past_6_month_btn);
        pastYear = findViewById(R.id.past_year_btn);
    }

    @Override
    public void onStart() {
        super.onStart();

        pastMonth.setOnClickListener(view -> {
            startActivity(new Intent(this, Wrapped1.class).putExtra("time", Wrapped.TimeRange.MONTH.toString()));
        });

        past6Months.setOnClickListener(view -> {
            startActivity(new Intent(this, Wrapped1.class).putExtra("time", Wrapped.TimeRange.SIX_MONTHS.toString()));
        });

        pastYear.setOnClickListener(view -> {
            startActivity(new Intent(this, Wrapped1.class).putExtra("time", Wrapped.TimeRange.YEAR.toString()));
        });
    }
}
