package com.example.cs2340project2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340project2.ui.wraps.WrappedActivity;
import com.example.cs2340project2.utils.WrapData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastWraps extends AppCompatActivity implements PastWrapRecyclerViewInterface {

    private List<PastWrapItem> wrapItemList;
    private RecyclerView wrapRecylcerView;
    private PastWrapAdapter pastWrapAdapter;
    private FirebaseFirestore fstore;
    private String userID;

    public void onCreate(Bundle savedInstanceState) {

        fstore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        wrapItemList = new ArrayList<>();

        DocumentReference documentReference = fstore.collection("pastwraps").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        for (int i = 0; i < map.size(); i++) {
                            Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(Integer.toString(i));
                            WrapData dummy = new WrapData(wrapDataMap);
                            wrapItemList.add(new PastWrapItem(dummy.getTopArtists().get(i).getImageUrl(), dummy.getDate(), dummy.getTimeRange()));
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

                setContentView(R.layout.pastwraps);

                wrapRecylcerView = findViewById(R.id.wrapRecyclerView);
                wrapRecylcerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                pastWrapAdapter = new PastWrapAdapter(wrapItemList, PastWraps.this);
                wrapRecylcerView.setAdapter(pastWrapAdapter);

            }
        });


        super.onCreate(savedInstanceState);
    }


    @Override
    public void onItemClick(int position) {
        PastWrapItem pastWrapItem = wrapItemList.get(position);

        changeActivity(position);
    }

    private void changeActivity(int position) {
        DocumentReference documentReference = fstore.collection("pastwraps").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(Integer.toString(position));
                        WrapData dummy = new WrapData(wrapDataMap);

                        Intent intent = new Intent(PastWraps.this, WrappedActivity.class);

                        intent.putExtra("timeRange", dummy.getTimeRange());
                        intent.putExtra("topArtists", (Serializable) dummy.getTopArtists());
                        intent.putExtra("topSongs", (Serializable) dummy.getTopSongs());
                        intent.putExtra("generatedDate", dummy.getDate());

                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
