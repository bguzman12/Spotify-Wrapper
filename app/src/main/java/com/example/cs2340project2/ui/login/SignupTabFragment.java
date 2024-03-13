package com.example.cs2340project2.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cs2340project2.R;

public class SignupTabFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_frag, container, false);

        return root;
    }
    // TODO : Button- go to get token, and store a token
    // TODO : also need to store username and password, all within firebase
    // TODO : Redirect- to to main home screen (within button click)
}
