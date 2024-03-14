package com.example.cs2340project2.ui.login;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LoginAdapter extends FragmentStateAdapter {
    private static final String[] TAB_TITLES = new String[]{"Login", "Signup"};
    public LoginAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LoginTabFragment();
            case 1:
                return new SignupTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }

    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
