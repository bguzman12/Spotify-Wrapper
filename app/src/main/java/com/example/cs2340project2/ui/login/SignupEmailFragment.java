package com.example.cs2340project2.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupEmailFragment extends Fragment {
    private SignupViewModel signupViewModel;
    private TextInputLayout emailLayout;
    private TextInputEditText emailText;
    private MaterialButton next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emailLayout = getView().findViewById(R.id.email_layout);
        emailText = getView().findViewById(R.id.email_input);
        next = getView().findViewById(R.id.next_btn);
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (validate()) {
            enableButton();
        }

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validate()) {
                    enableButton();
                } else {
                    disableButton();
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()) {
                        emailLayout.setError("Email address is not a valid email address");
                    } else {
                        emailLayout.setError("");
                    }
                }
            }
        });

        next.setOnClickListener(view -> {
            signupViewModel.setEmail(emailText.getText().toString());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.signup_fragment_container, SignupPasswordFragment.class, null, "signupPassword")
                    .addToBackStack("signupPassword")
                    .commit();
        });
    }

    private boolean validate() {
        return Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches();
    }

    private void enableButton() {
        next.setEnabled(true);
        next.setBackgroundColor(Color.WHITE);
        emailLayout.setError("");
    }

    private void disableButton() {
        next.setEnabled(false);
        next.setBackgroundColor(Color.parseColor("#414141"));
    }
}
