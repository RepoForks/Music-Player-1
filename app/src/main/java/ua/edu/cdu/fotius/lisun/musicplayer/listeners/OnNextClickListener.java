package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

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
