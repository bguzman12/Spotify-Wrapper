package com.example.cs2340project2.ui.wraps;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340project2.R;
import com.example.cs2340project2.utils.ArtistInfo;
import com.example.cs2340project2.utils.SongInfo;
import com.example.cs2340project2.utils.WrapViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class WrappedTopSingleFragment extends Fragment {
    private TextView title;
    private ImageView image;
    private TextView name;
    private TextView sub;
    private WrapViewModel wrapViewModel;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrapped_top_single, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = requireView().findViewById(R.id.my_top_blank);
        image = requireView().findViewById(R.id.top_image);
        name = requireView().findViewById(R.id.top_blank);
        sub = requireView().findViewById(R.id.top_blank_sub);
        wrapViewModel = new ViewModelProvider(requireActivity()).get(WrapViewModel.class);
        type = getParentFragmentManager().getBackStackEntryAt(getParentFragmentManager().getBackStackEntryCount() - 1).getName();
    }

    @Override
    public void onStart() {
        super.onStart();

        String titleString;
        String imageURL;
        String nameString;
        String subString;
        String clip;

        switch (type) {
            case "topSong":
                titleString = "My Top Song";
                SongInfo song = wrapViewModel.getTopSongs().get(0);
                imageURL = song.getImageUrl();
                nameString = song.getName();
                subString = song.getArtist();
                clip = wrapViewModel.getTopSongs().get(0).getClip();
                break;
            case "topArtist":
                titleString = "My Top Artist";
                ArtistInfo artist = wrapViewModel.getTopArtists().get(0);
                imageURL = artist.getImageUrl();
                nameString = artist.getArtist();
                subString = "";
                clip = wrapViewModel.getTopSongs().get(1).getClip();
                break;
            default:
                titleString = "";
                imageURL = "";
                nameString = "";
                subString = "";
                clip = "";
                break;
        }

        try {
            wrapViewModel.getMediaPlayer().reset();
            wrapViewModel.getMediaPlayer().setDataSource(clip);
            if (!wrapViewModel.isPaused()) {
                wrapViewModel.getMediaPlayer().setOnPreparedListener(MediaPlayer::start);
            } else {
                wrapViewModel.getMediaPlayer().setOnPreparedListener((listener) -> {
                    wrapViewModel.getMediaPlayer().start();
                    wrapViewModel.getMediaPlayer().pause();
                });
            }
            wrapViewModel.getMediaPlayer().prepareAsync();
        } catch (IOException e) {
            Snackbar.make(requireView(), "Unable to play audio clip", Snackbar.LENGTH_SHORT).show();
        }



        requireActivity().runOnUiThread(() -> {
            title.setText(titleString);
            Picasso.get().load(imageURL).into(image);
            name.setText(nameString);
        });

        if (subString.isEmpty()) {
            sub.setVisibility(View.GONE);
        } else {
            requireActivity().runOnUiThread(() -> sub.setText(subString));
        }
    }
}
