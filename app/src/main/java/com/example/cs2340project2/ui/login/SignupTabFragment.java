package com.example.cs2340project2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

import javax.annotation.Nullable;

public class SignupTabFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpBtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_frag, container, false);

        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        email = getView().findViewById(R.id.signupEmail);
        password = getView().findViewById(R.id.signupPassword);
        confirmPassword = getView().findViewById(R.id.confirmSignupPassword);
        signUpBtn = getView().findViewById(R.id.signup_btn);
    }

    @Override
    public void onStart() {
        super.onStart();

        signUpBtn.setOnClickListener(view -> {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                Toast.makeText(getContext(), "Email address is invalid.", Toast.LENGTH_SHORT).show();
            } else {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getContext(), "The passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString());
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
    }
}
