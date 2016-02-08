package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnShowQueueClickListener;

public class PlaybackHeaderFragment extends Fragment implements ServiceConnectionObserver{

    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public PlaybackHeaderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playback_toolbar, container, false);
        ImageButton showQueue = (ImageButton) v.findViewById(R.id.show_current_queue);
        showQueue.setOnClickListener(new OnShowQueueClickListener(getActivity(), mServiceWrapper));
        return v;
    }

    @Override
    public void ServiceConnected() {
        //TODO:
    }

    @Override
    public void ServiceDisconnected() {
        //TODO:
    }
}
