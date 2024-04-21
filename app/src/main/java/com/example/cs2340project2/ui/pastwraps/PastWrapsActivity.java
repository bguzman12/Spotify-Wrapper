package com.example.cs2340project2.ui.pastwraps;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340project2.R;
import com.example.cs2340project2.ui.wraps.WrappedActivity;
import com.example.cs2340project2.utils.WrapData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastWrapsActivity extends AppCompatActivity implements PastWrapRecyclerViewInterface {

    private List<PastWrapItem> wrapItemList;
    private RecyclerView wrapRecyclerView;
    private MaterialToolbar toolbar;
    private FirebaseFirestore db;
    private String userID;
    private PastWrapAdapter pastWrapAdapter;
    private DocumentReference ref;
    private CoordinatorLayout coordinatorLayout;

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

            pastWrapAdapter = new PastWrapAdapter(wrapItemList, PastWrapsActivity.this);
            wrapRecyclerView.setAdapter(pastWrapAdapter);

            coordinatorLayout = findViewById(R.id.pastWraps_container);

            ItemTouchHelper helper = new ItemTouchHelper(callback);
            helper.attachToRecyclerView(wrapRecyclerView);
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

                    Intent intent = new Intent(PastWrapsActivity.this, WrappedActivity.class);

                    intent.putExtra("timeRange", dummy.getTimeRange());
                    intent.putExtra("topArtists", (Serializable) dummy.getTopArtists());
                    intent.putExtra("topSongs", (Serializable) dummy.getTopSongs());
                    intent.putExtra("generatedDate", dummy.getDate());

                    startActivity(intent);
                }
            }
        });
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            DocumentReference documentReference = db.collection("pastwraps").document(userID);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(Integer.toString(viewHolder.getAdapterPosition()));
                        WrapData dummy = new WrapData(wrapDataMap);

                        if (dummy.getPosted()) {
                            Snackbar.make(coordinatorLayout, "This wrap is already public!", Snackbar.LENGTH_LONG).show();
                            pastWrapAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            return;
                        }

                        dummy.setPosted(true);
                        Map<String, Object> map = new HashMap<>(document.getData());
                        map.put(Integer.toString(viewHolder.getAdapterPosition()), dummy);
                        documentReference.set(map);

                        DocumentReference newDocumentReference = db.collection("pastwraps").document("public");

                        newDocumentReference.get().addOnCompleteListener(pubTask -> {
                            Map<String, Object> pubMap = new HashMap<>();
                            int numWraps = 0;
                            if (pubTask.isSuccessful()) {
                                DocumentSnapshot newDocument = pubTask.getResult();
                                if (newDocument.exists()) {
                                    numWraps = newDocument.getData().size();
                                    pubMap.putAll(newDocument.getData());
                                }
                            }

                            dummy.setPosition(viewHolder.getAdapterPosition());
                            pubMap.put(Integer.toString(numWraps), dummy);
                            newDocumentReference.set(pubMap);
                            Snackbar.make(coordinatorLayout, "Wrap Posted", Snackbar.LENGTH_LONG).show();
                            pastWrapAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        });
                    }
                }
            });

        }
    };
}
