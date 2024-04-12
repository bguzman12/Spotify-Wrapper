package com.example.cs2340project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PastWraps extends AppCompatActivity implements PastWrapRecyclerViewInterface {

    private List<PastWrapItem> wrapItemList;
    private RecyclerView wrapRecylcerView;
    private PastWrapAdapter pastWrapAdapter;

    public PastWraps() {

    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastwraps);


        wrapItemList = PastWrapList.getPastWrapItems();

        wrapItemList.add(new PastWrapItem("Test", "test"));

        wrapItemList.add(new PastWrapItem("Test2", "test2"));

        wrapRecylcerView = findViewById(R.id.wrapRecyclerView);
        wrapRecylcerView.setLayoutManager(new LinearLayoutManager(this));

        pastWrapAdapter = new PastWrapAdapter(wrapItemList, this);
        wrapRecylcerView.setAdapter(pastWrapAdapter);

    }


    public static class PastWrapList {
        private static ArrayList<PastWrapItem> pastWrapItems = new ArrayList<>();

        public static ArrayList<PastWrapItem> getPastWrapItems() {
            return pastWrapItems;
        }
    }

    @Override
    public void onItemClick(int position) {
        PastWrapItem pastWrapItem = wrapItemList.get(position);

        changeActivity(position);
    }

    private void changeActivity(int position) {
        Intent intent = new Intent(this, PastWrapped1.class);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }
}
