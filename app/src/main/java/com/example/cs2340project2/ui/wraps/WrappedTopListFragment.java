package com.example.cs2340project2.ui.wraps;

import android.media.MediaPlayer;
import android.os.Bundle;
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
import java.util.List;

public class WrappedTopListFragment extends Fragment {
    private TextView title;
    private ImageView[] images;
    private TextView[] names;
    private TextView[] subnames;
    private WrapViewModel wrapViewModel;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrapped_top_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = requireView().findViewById(R.id.my_top_blanks);
        images = new ImageView[5];
        images[0] = requireView().findViewById(R.id.my_top_image_one);
        images[1] = requireView().findViewById(R.id.my_top_image_two);
        images[2] = requireView().findViewById(R.id.my_top_image_three);
        images[3] = requireView().findViewById(R.id.my_top_image_four);
        images[4] = requireView().findViewById(R.id.my_top_image_five);
        names = new TextView[5];
        names[0] = requireView().findViewById(R.id.my_top_one);
        names[1] = requireView().findViewById(R.id.my_top_two);
        names[2] = requireView().findViewById(R.id.my_top_three);
        names[3] = requireView().findViewById(R.id.my_top_four);
        names[4] = requireView().findViewById(R.id.my_top_five);
        subnames = new TextView[5];
        subnames[0] = requireView().findViewById(R.id.my_top_one_sub);
        subnames[1] = requireView().findViewById(R.id.my_top_two_sub);
        subnames[2] = requireView().findViewById(R.id.my_top_three_sub);
        subnames[3] = requireView().findViewById(R.id.my_top_four_sub);
        subnames[4] = requireView().findViewById(R.id.my_top_five_sub);
        wrapViewModel = new ViewModelProvider(requireActivity()).get(WrapViewModel.class);
        type = getParentFragmentManager().getBackStackEntryAt(getParentFragmentManager().getBackStackEntryCount() - 1).getName();
    }

    @Override
    public void onStart() {
        super.onStart();

        String clip;
        String titleName;

        switch (type) {
            case "topSongs":
                List<SongInfo> songs = wrapViewModel.getTopSongs();
                titleName = "My Top Songs";
                for (int i = 0; i < 5; i++) {
                    SongInfo song = songs.get(i);
                    String songName = song.getName();
                    String albumImage = song.getImageUrl();
                    String artistName = song.getArtist();
                    final int j = i;
                    requireActivity().runOnUiThread(() -> {
                        Picasso.get().load(albumImage).into(images[j]);
                        names[j].setText(songName);
                        subnames[j].setText(artistName);
                    });
                }
                clip = songs.get(2).getClip();
                break;
            case "topArtists":
                List<ArtistInfo> artists = wrapViewModel.getTopArtists();
                titleName = "My Top Artists";
                for (int i = 0; i < 5; i++) {
                    ArtistInfo artist = artists.get(i);
                    String artistName = artist.getArtist();
                    String artistImage = artist.getImageUrl();
                    final int j = i;
                    requireActivity().runOnUiThread(() -> {
                        Picasso.get().load(artistImage).into(images[j]);
                        names[j].setText(artistName);
                        subnames[j].setVisibility(View.GONE);
                    });
                }
                clip = wrapViewModel.getTopSongs().get(3).getClip();
                break;
            default:
                clip = "";
                titleName = "";
        }

        requireActivity().runOnUiThread(() -> title.setText(titleName));

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
    }
}
