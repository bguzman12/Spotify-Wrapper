package com.example.cs2340project2;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class PastWrapItem {
    private Bitmap summaryBitmap;
    private String date;
    private String time;

    public PastWrapItem(Bitmap summaryBitmap) {
        this.summaryBitmap = summaryBitmap;
    }

    public Bitmap getSummaryBitmap() {
        return summaryBitmap;
    }

    public void setSummaryBitmap(Bitmap summaryBitmap) {
        this.summaryBitmap = summaryBitmap;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
