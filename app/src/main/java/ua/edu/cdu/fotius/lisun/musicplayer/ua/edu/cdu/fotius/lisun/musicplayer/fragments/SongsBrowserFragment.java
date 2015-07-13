package ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsBrowserFragment extends ListFragment {

    public SongsBrowserFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs_browser, container, false);
    }


}
