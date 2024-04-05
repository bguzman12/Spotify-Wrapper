package com.example.cs2340project2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cs2340project2.Homescreen;
import com.example.cs2340project2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import javax.annotation.Nullable;

public class LoginTabFragment extends Fragment {
    private EditText username;
    private EditText password;
    private Button loginBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_frag, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        username = getView().findViewById(R.id.loginUsername);
        password = getView().findViewById(R.id.loginPassword);
        loginBtn = getView().findViewById(R.id.login_btn);
    }

    public boolean validateEmail() {
        String emailStr = username.getText().toString();
        if (emailStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePassword() {
        String passwordStr = password.getText().toString();
        if (passwordStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void checkUser() {
        String userUsername = username.getText().toString();
        String userPassword = password.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        Intent intent = new Intent(getActivity(), Homescreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        loginBtn.setOnClickListener(view -> {
            if (validateEmail() && validatePassword()) {
                checkUser();
            }
        });
    }

    /**
     * check that all fields contain text
     * @return true if all fields are full
     */
    private boolean checkLoginFull() {
        return !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty();
    }
}
