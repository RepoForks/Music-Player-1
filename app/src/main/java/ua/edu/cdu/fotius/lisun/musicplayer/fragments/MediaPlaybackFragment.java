package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaPlaybackFragment extends Fragment {


    private MediaPlaybackServiceWrapper mServiceWrapper;

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindService(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_playback, container, false);
    }


}
