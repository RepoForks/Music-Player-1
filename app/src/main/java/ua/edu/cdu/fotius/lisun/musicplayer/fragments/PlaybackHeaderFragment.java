package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnShowQueueClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.lyrics.LyricsActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceConnectionObserver;

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
        Log.d(PlaybackHeaderFragment.class.getSimpleName(), "onCreateView. ");
        View v = inflater.inflate(R.layout.fragment_playback_toolbar, container, false);
        ButterKnife.bind(this, v);
        ImageButton showQueue = (ImageButton) v.findViewById(R.id.show_current_queue);
        showQueue.setOnClickListener(new OnShowQueueClickListener(getActivity()));
        return v;
    }

    @OnClick(R.id.ib_show_lyrics)
    public void showLyrics() {
        Log.d(PlaybackHeaderFragment.class.getSimpleName(), "showLyrics. ");
        Intent intent = new Intent(getActivity(), LyricsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(LyricsActivity.KEY_ARTIST, mServiceWrapper.getArtistName());
        bundle.putString(LyricsActivity.KEY_SONG, mServiceWrapper.getTrackName());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
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
