package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.views.PlayPauseButton;

public class OnPlayPauseClickListener implements View.OnClickListener{

    private MediaPlaybackServiceWrapper mServiceWrapper;

    public OnPlayPauseClickListener(MediaPlaybackServiceWrapper serviceWrapper) {
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
