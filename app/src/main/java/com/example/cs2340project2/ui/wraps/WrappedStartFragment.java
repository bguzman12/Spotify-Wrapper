package com.example.cs2340project2.ui.wraps;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.R;
import com.example.cs2340project2.utils.WrapViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WrappedStartFragment extends Fragment {
    private TextView timeRangeText;
    private TextView generatedDate;
    private final Map<String, String> timeMap = new HashMap<>();
    private WrapViewModel wrapViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrapped_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        timeRangeText = requireView().findViewById(R.id.wrapped_start_timerange);
        generatedDate = requireView().findViewById(R.id.generated_date);
        wrapViewModel = new ViewModelProvider(requireActivity()).get(WrapViewModel.class);
        timeMap.put("short_term", "month!");
        timeMap.put("medium_term", "6 months!");
        timeMap.put("long_term", "year!");
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            wrapViewModel.getMediaPlayer().reset();
            String clip = wrapViewModel.getTopSongs().get(4).getClip();
            wrapViewModel.getMediaPlayer().setDataSource(clip);
            wrapViewModel.getMediaPlayer().prepareAsync();
            if (!wrapViewModel.isPaused()) {
                wrapViewModel.getMediaPlayer().setOnPreparedListener(MediaPlayer::start);
            } else {
                wrapViewModel.getMediaPlayer().setOnPreparedListener(MediaPlayer::pause);
            }
        } catch (IOException e) {
            Snackbar.make(requireView(), "Unable to play audio clip", Snackbar.LENGTH_SHORT).show();
        }


        requireActivity().runOnUiThread(() -> {
            timeRangeText.setText(timeMap.get(wrapViewModel.getTimeRange()));
            generatedDate.setText(String.format("Generated on %s", wrapViewModel.getGeneratedDate()));
        });
    }

}
