package com.example.cs2340project2.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cs2340project2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.annotation.Nullable;

public class SignupTabFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_frag, container, false);


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
            if (checkSignupFull()) {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getContext(), "The passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    // Call getToken() to initiate token retrieval
                    ((LoginActivity) getActivity()).getToken();
                    // No need to immediately get token here
                    // Handle token availability in the callback method
                }
            } else {
                Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * check that all fields contain text
     * @return true if all fields are full
     */
    private boolean checkSignupFull() {
        return !email.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()
                && !confirmPassword.getText().toString().trim().isEmpty();
    }




}