package ua.edu.cdu.fotius.lisun.musicplayer.ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaylistsBrowserFragment extends Fragment {


    public MyPlaylistsBrowserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_playlists_browser, container, false);
    }


}
