package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public abstract class BaseRepeatShuffleClickListener implements View.OnClickListener{

    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public BaseRepeatShuffleClickListener(MediaPlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onClick(View clickedView) {
        setMode(clickedView);
    }

    public abstract void setMode(View clickedView);
}
