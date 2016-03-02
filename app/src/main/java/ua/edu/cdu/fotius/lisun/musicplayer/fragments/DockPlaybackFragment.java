package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class DockPlaybackFragment extends BasePlaybackFragment {

    public DockPlaybackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_dock_playback;
    }
}
