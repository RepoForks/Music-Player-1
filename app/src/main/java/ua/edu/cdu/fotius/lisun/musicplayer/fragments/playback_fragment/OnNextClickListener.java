package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public class OnNextClickListener implements View.OnClickListener {

    private PlaybackServiceWrapper mServiceWrapper;

    public OnNextClickListener(PlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onClick(View v) {
        mServiceWrapper.next();
    }
}
