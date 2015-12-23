package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;

public class PlaybackToolbarFragment extends Fragment implements ServiceConnectionObserver{

    public static final String TAG = "playback_toolbar_fragment";

    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public PlaybackToolbarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);

        Log.e(TAG.substring(0, 23), "PlaybackToolbarFragment.onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG.substring(0, 23), "PlaybackToolbarFragment.onDestroy()");
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
