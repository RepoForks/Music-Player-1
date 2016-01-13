package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.PlayPauseButton;

public class OnPlayPauseClickListener implements View.OnClickListener{

    private PlaybackServiceWrapper mServiceWrapper;

    public OnPlayPauseClickListener(PlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onClick(View v) {
        boolean isPlaying = mServiceWrapper.isPlaying();
        if(isPlaying) {
            mServiceWrapper.pause();
        } else {
            mServiceWrapper.play();
        }
        PlayPauseButton button = (PlayPauseButton) v;
        button.updateStateImage(isPlaying);
    }
}
