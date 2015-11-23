package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class OnPlayPauseClickListener implements View.OnClickListener{

    private MediaPlaybackServiceWrapper mServiceWrapper;
    private PlaybackViewsStateListener mPlaybackViewsStateListener;

    public OnPlayPauseClickListener(MediaPlaybackServiceWrapper serviceWrapper, PlaybackViewsStateListener playbackViewsStateListener) {
        mServiceWrapper = serviceWrapper;
        mPlaybackViewsStateListener = playbackViewsStateListener;
    }

    @Override
    public void onClick(View v) {
        playPause();
        mPlaybackViewsStateListener.updatePlayPauseButtonImage();
    }

    private void playPause() {
        if(mServiceWrapper.isPlaying()) {
            mServiceWrapper.pause();
        } else {
            mServiceWrapper.play();
        }
    }
}
