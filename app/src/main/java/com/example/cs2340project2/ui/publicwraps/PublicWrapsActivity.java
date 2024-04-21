package com.example.cs2340project2.ui.publicwraps;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicWrapsActivity extends AppCompatActivity implements PublicWrapRecyclerViewInterface {

    private List<PublicWrapItem> wrapItemList;
    private RecyclerView wrapRecyclerView;
    private MaterialToolbar toolbar;
    private FirebaseFirestore db;
    private String userID;
    private PublicWrapAdapter publicWrapAdapter;
    private DocumentReference ref;
    private CoordinatorLayout coordinatorLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicwraps);

        toolbar = findViewById(R.id.topAppBar_publicWraps);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void onStart() {
        super.onStart();

        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        wrapItemList = new ArrayList<>();
        ref = db.collection("pastwraps").document("public");
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> map = document.getData();
                    for (int i = 0; i < map.size(); i++) {
                        Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(String.format("%d", i));
                        WrapData dummy = new WrapData(wrapDataMap);
                        wrapItemList.add(new PublicWrapItem(dummy.getTopArtists().get(0).getImageUrl(), dummy.getDate(), dummy.getTimeRange(), dummy.getAuthor()));
                    }
                }
            }

            wrapRecyclerView = findViewById(R.id.wrapRecyclerView);
            wrapRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            publicWrapAdapter = new PublicWrapAdapter(wrapItemList, PublicWrapsActivity.this);
            wrapRecyclerView.setAdapter(publicWrapAdapter);

            coordinatorLayout = findViewById(R.id.publicWraps_container);
            ItemTouchHelper helper = new ItemTouchHelper(callback);
            helper.attachToRecyclerView(wrapRecyclerView);
        });
    }


    @Override
    public void onItemClick(int position) {
        changeActivity(position);
    }

    private void changeActivity(int position) {
        DocumentReference documentReference = db.collection("pastwraps").document("public");
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> wrapDataMap = (Map<String, Object>) document.get(Integer.toString(position));
                    WrapData dummy = new WrapData(wrapDataMap);

                    Intent intent = new Intent(PublicWrapsActivity.this, WrappedActivity.class);

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
            DocumentReference documentReference = db.collection("pastwraps").document("public");
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot pubDocument = task.getResult();
                    if (pubDocument.exists()) {
                        Map<String, Object> wrapDataMap = (Map<String, Object>) pubDocument.get(Integer.toString(viewHolder.getAdapterPosition()));
                        if (wrapDataMap == null) {
                            return;
                        }
                        WrapData dummy = new WrapData(wrapDataMap);

                        if (!dummy.getAuthor().equals(userID)) {
                            Snackbar.make(coordinatorLayout, "Can't delete other user's wraps", Snackbar.LENGTH_LONG).show();
                            publicWrapAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            return;
                        }

                        db.collection("tokens").document(userID).update("public_posts", FieldValue.increment(-1));

                        dummy.setPosted(false);
                        Map<String, Object> map = new HashMap<>();

                        int j = viewHolder.getAdapterPosition();
                        for (int i = j; i < pubDocument.getData().size() - 1; i++) {
                            map.put(Integer.toString(i), pubDocument.getData().get(Integer.toString(i + 1)));
                        }

                        documentReference.set(map);

                        DocumentReference newDocumentReference = db.collection("pastwraps").document(userID);
                        newDocumentReference.get().addOnCompleteListener(newTask -> {
                            Map<String, Object> newMap = new HashMap<>();
                            if (task.isSuccessful()) {
                                DocumentSnapshot newDocument = newTask.getResult();
                                if (newDocument.exists()) {
                                    newMap.putAll(newDocument.getData());
                                }
                            }

                            newMap.put(Integer.toString(dummy.getPosition()), dummy);
                            newDocumentReference.set(newMap);

                            Snackbar.make(coordinatorLayout, "Wrap is no longer public", Snackbar.LENGTH_LONG).show();

                            wrapItemList.remove(viewHolder.getAdapterPosition());
                            publicWrapAdapter.notifyItemRemoved(j);
                        });
                    }
                }
            });

        }
    };
}
