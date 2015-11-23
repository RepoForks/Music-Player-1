package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public abstract class BaseRepeatShuffleClickListener implements View.OnClickListener{

    protected MediaPlaybackServiceWrapper mServiceWrapper;
    protected PlaybackViewsStateListener mPlaybackViewsStateListener;

    public BaseRepeatShuffleClickListener(MediaPlaybackServiceWrapper serviceWrapper,
                                          PlaybackViewsStateListener playbackViewsStateListener) {
        mServiceWrapper = serviceWrapper;
        mPlaybackViewsStateListener = playbackViewsStateListener;
    }

    @Override
    public void onClick(View clickedView) {
        setMode(clickedView);
    }

    public abstract void setMode(View clickedView);
}
