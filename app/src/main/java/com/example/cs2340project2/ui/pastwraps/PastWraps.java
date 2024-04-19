package com.example.cs2340project2.ui.pastwraps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340project2.R;
import com.example.cs2340project2.ui.wraps.WrappedActivity;
import com.example.cs2340project2.utils.WrapData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PastWraps extends AppCompatActivity implements PastWrapRecyclerViewInterface {

    private List<PastWrapItem> wrapItemList;
    private RecyclerView wrapRecyclerView;
    private MaterialToolbar toolbar;
    private FirebaseFirestore db;
    private String userID;
    private PastWrapAdapter pastWrapAdapter;
    private DocumentReference ref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastwraps);

        toolbar = findViewById(R.id.topAppBar_pastWraps);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void onStart() {
        super.onStart();

        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        wrapItemList = new ArrayList<>();
        ref = db.collection("pastwraps").document(userID);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> map = document.getData();
                    for (int i = 0; i < map.size(); i++) {
                        Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(String.format("%d", i));
                        WrapData dummy = new WrapData(wrapDataMap);
                        wrapItemList.add(new PastWrapItem(dummy.getTopArtists().get(0).getImageUrl(), dummy.getDate(), dummy.getTimeRange()));
                    }
                }
            }

            wrapRecyclerView = findViewById(R.id.wrapRecyclerView);
            wrapRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            pastWrapAdapter = new PastWrapAdapter(wrapItemList, PastWraps.this);
            wrapRecyclerView.setAdapter(pastWrapAdapter);
        });
    }


    @Override
    public void onItemClick(int position) {
        changeActivity(position);
    }

    private void changeActivity(int position) {
        DocumentReference documentReference = db.collection("pastwraps").document(userID);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(Integer.toString(position));
                    WrapData dummy = new WrapData(wrapDataMap);

                    Intent intent = new Intent(PastWraps.this, WrappedActivity.class);

                    intent.putExtra("timeRange", dummy.getTimeRange());
                    intent.putExtra("topArtists", (Serializable) dummy.getTopArtists());
                    intent.putExtra("topSongs", (Serializable) dummy.getTopSongs());
                    intent.putExtra("generatedDate", dummy.getDate());

                    startActivity(intent);
                }
            }
        });
    }
}
