package com.example.cs2340project2.ui.wraps;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class WrappedSummaryFragment extends Fragment {
    private ImageView topArtist;
    private TextView[] artists;
    private TextView[] songs;
    private WrapViewModel wrapViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrapped_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        topArtist = requireView().findViewById(R.id.top_image);
        artists = new TextView[5];
        artists[0] = requireView().findViewById(R.id.top_artist_one);
        artists[1] = requireView().findViewById(R.id.top_artist_two);
        artists[2] = requireView().findViewById(R.id.top_artist_three);
        artists[3] = requireView().findViewById(R.id.top_artist_four);
        artists[4] = requireView().findViewById(R.id.top_artist_five);
        songs = new TextView[5];
        songs[0] = requireView().findViewById(R.id.top_song_one);
        songs[1] = requireView().findViewById(R.id.top_song_two);
        songs[2] = requireView().findViewById(R.id.top_song_three);
        songs[3] = requireView().findViewById(R.id.top_song_four);
        songs[4] = requireView().findViewById(R.id.top_song_five);
        wrapViewModel = new ViewModelProvider(requireActivity()).get(WrapViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        List<SongInfo> songsList = wrapViewModel.getTopSongs();
        List<ArtistInfo> artistsList = wrapViewModel.getTopArtists();
        Picasso.get().load(artistsList.get(0).getImageUrl()).into(topArtist);
        for (int i = 0; i < 5; i++) {
            SongInfo song = songsList.get(i);
            ArtistInfo artist = artistsList.get(i);
            String songName = song.getName();
            String artistName = artist.getArtist();
            final int j = i;
            requireActivity().runOnUiThread(() -> {
                artists[j].setText(artistName);
                songs[j].setText(songName);
            });
        }
    }
}
