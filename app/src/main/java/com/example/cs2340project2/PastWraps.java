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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PastWrapViewModel homeViewModel =
                new ViewModelProvider(this).get(PastWrapViewModel.class);


        View rootView = inflater.inflate(R.layout.pastwraps, container, false);
        wrapItemList = PastWrapList.getPastWrapItems();


        wrapRecylcerView = rootView.findViewById(R.id.wrapRecyclerView);
        wrapRecylcerView.setLayoutManager(new LinearLayoutManager(this));

        pastWrapAdapter = new PastWrapAdapter(wrapItemList, this);
        wrapRecylcerView.setAdapter(pastWrapAdapter);


        return rootView;
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
        Intent intent = new Intent(this, Wrapped1.class);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }
}
