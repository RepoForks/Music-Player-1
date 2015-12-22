package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class DockPlaybackFragment extends AbstractPlaybackFragment{

    private final String TAG = getClass().getSimpleName();

    public DockPlaybackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "DockPlaybackFragment.onCreate()");
        super.onCreate(savedInstanceState);
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
