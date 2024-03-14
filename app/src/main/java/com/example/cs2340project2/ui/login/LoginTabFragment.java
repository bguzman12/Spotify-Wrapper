package com.example.cs2340project2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cs2340project2.MainActivity;
import com.example.cs2340project2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public class LoginTabFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button loginBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_frag, container, false);

        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        email = getView().findViewById(R.id.loginEmail);
        password = getView().findViewById(R.id.loginPassword);
        loginBtn = getView().findViewById(R.id.login_btn);
    }

    @Override
    public void onStart() {
        super.onStart();

        loginBtn.setOnClickListener(view -> {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
            });
    }
}
