package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class OnNextClickListener implements View.OnClickListener {

    private MediaPlaybackServiceWrapper mServiceWrapper;

    public OnNextClickListener(MediaPlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onClick(View v) {
        mServiceWrapper.next();
    }
}
