package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;

public class PlaybackToolbarFragment extends Fragment implements ServiceConnectionObserver{

    protected PlaybackServiceWrapper mServiceWrapper;

    public PlaybackToolbarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mServiceWrapper = PlaybackServiceWrapper.getInstance();
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
