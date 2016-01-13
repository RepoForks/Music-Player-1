package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public abstract class BaseRepeatShuffleClickListener implements View.OnClickListener{

    protected PlaybackServiceWrapper mServiceWrapper;

    public BaseRepeatShuffleClickListener(PlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onClick(View clickedView) {
        setMode(clickedView);
    }

    public abstract void setMode(View clickedView);
}
