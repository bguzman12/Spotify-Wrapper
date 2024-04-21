package com.example.cs2340project2;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PastWraps extends AppCompatActivity implements PastWrapRecyclerViewInterface {

    private List<PastWrapItem> wrapItemList;
    private RecyclerView wrapRecylcerView;
    private PastWrapAdapter pastWrapAdapter;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastwraps);


        wrapItemList = PastWrapList.getPastWrapItems();

        wrapRecylcerView = findViewById(R.id.wrapRecyclerView);
        wrapRecylcerView.setLayoutManager(new LinearLayoutManager(this));

        pastWrapAdapter = new PastWrapAdapter(wrapItemList, this);
        wrapRecylcerView.setAdapter(pastWrapAdapter);

    }


    public static class PastWrapList {
        private static ArrayList<PastWrapItem> pastWrapItems = new ArrayList<>();

        public static ArrayList<PastWrapItem> getPastWrapItems() {
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://cs-2340-project-2-6ffe6.appspot.com");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            StorageReference storageRef = storage.getReference();
            StorageReference listRef = storageRef.child(currentUser.getUid());
            listRef.listAll()
                    .addOnSuccessListener(listResult -> {
                        for (StorageReference imageRef : listResult.getItems()) {
                            try {
                                File localFile = File.createTempFile(imageRef.getName() + "_past", "png");
                                imageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                                    pastWrapItems.add(new PastWrapItem(BitmapFactory.decodeFile(localFile.getAbsolutePath())));
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
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
