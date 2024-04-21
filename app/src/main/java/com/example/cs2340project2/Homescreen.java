package com.example.cs2340project2;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340project2.databinding.ActivityHomescreenBinding;
import com.example.cs2340project2.ui.editlogin.EditLoginActivity;
import com.example.cs2340project2.ui.pastwraps.PastWraps;

public class Homescreen extends AppCompatActivity {

    private ActivityHomescreenBinding binding;
    private boolean isExpanded = false;

    private Animation fromBottomFabAnim, toBottomFabAnim, rotateClockWiseFabAnim, rotateAntiClockWiseFabAnim,
            fromBottomBgAnim, toBottomBgAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        binding = ActivityHomescreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize animations
        fromBottomFabAnim = AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab);
        toBottomFabAnim = AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab);
        rotateClockWiseFabAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise);
        rotateAntiClockWiseFabAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise);
        fromBottomBgAnim = AnimationUtils.loadAnimation(this, R.anim.from_bottom_animation);
        toBottomBgAnim = AnimationUtils.loadAnimation(this, R.anim.to_bottom_animation);
    }

    public void newWrappedClicked(View view) {
        startActivity(new Intent(this, TimeWrapped.class));
    }

    public void pastWrapsClicked(View view) {
        startActivity(new Intent(this, PastWraps.class));
    }

    public void editLoginClicked(View view) {
        startActivity(new Intent(this, EditLoginActivity.class));
    }
    public void mainFabBtnClicked(View view) {
        if (isExpanded) {
            shrinkFab();
        } else {
            expandFab();
        }
    }

    private void onGalleryClicked() {
        Toast.makeText(this, "Gallery Clicked", Toast.LENGTH_SHORT).show();
    }

    private void shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim);
        binding.buttonPastWraps.startAnimation(toBottomFabAnim);
        binding.buttonPublicWraps.startAnimation(toBottomFabAnim);
        binding.buttonNewWrapped.startAnimation(toBottomFabAnim);
        binding.pastwrapsTv.startAnimation(toBottomFabAnim);
        binding.publicwrapsTv.startAnimation(toBottomFabAnim);
        binding.createwrapsTv.startAnimation(toBottomFabAnim);
        isExpanded = false;
    }

    private void expandFab() {
        binding.transparentBg.startAnimation(fromBottomBgAnim);
        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim);
        binding.buttonPastWraps.startAnimation(fromBottomFabAnim);
        binding.buttonPublicWraps.startAnimation(fromBottomFabAnim);
        binding.buttonNewWrapped.startAnimation(fromBottomFabAnim);
        binding.pastwrapsTv.startAnimation(fromBottomFabAnim);
        binding.publicwrapsTv.startAnimation(fromBottomFabAnim);
        binding.createwrapsTv.startAnimation(fromBottomFabAnim);
        isExpanded = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isExpanded) {
                Rect outRect = new Rect();
                binding.fabConstraint.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    shrinkFab();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
