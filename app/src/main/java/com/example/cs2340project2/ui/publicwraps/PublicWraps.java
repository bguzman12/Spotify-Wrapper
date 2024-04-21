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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicWraps extends AppCompatActivity implements PublicWrapRecyclerViewInterface {

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

            publicWrapAdapter = new PublicWrapAdapter(wrapItemList, PublicWraps.this);
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

                    Intent intent = new Intent(PublicWraps.this, WrappedActivity.class);

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
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You can't delete other user's wraps!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            publicWrapAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            return;
                        }


                        dummy.setPosted(false);
                        Map<String, Object> map = new HashMap<>();
                        map.putAll(pubDocument.getData());

                        int j = viewHolder.getAdapterPosition();
                        for (int i = j; i < map.size() - 1; i++) {
                            map.put(Integer.toString(i), map.get(Integer.toString(j + 1)));
                            j++;
                        }
                        map.remove(Integer.toString(j));

                        documentReference.set(map);

                        DocumentReference newDocumentReference = db.collection("pastwraps").document(userID);
                        Map<String, Object> newMap = new HashMap<>();
                        final int[] numWraps = new int[1];
                        newDocumentReference.get().addOnCompleteListener(newTask -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot newDocument = newTask.getResult();
                                if (newDocument.exists()) {
                                    numWraps[0] = newDocument.getData().size();
                                    newMap.putAll(newDocument.getData());
                                } else {
                                    numWraps[0] = 0;
                                }
                            }
                            int position = dummy.getPosition();
                            newMap.put(Integer.toString(position), dummy);
                            newDocumentReference.set(newMap);



                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Public Wrap Deleted", Snackbar.LENGTH_LONG);
                            snackbar.show();

                            wrapItemList.remove(viewHolder.getAdapterPosition());
                            publicWrapAdapter.notifyDataSetChanged();
                        });
                    }
                }
            });

        }
    };
}
