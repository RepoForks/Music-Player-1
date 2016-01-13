package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public class OnPreviousClickListener implements View.OnClickListener{



    private PlaybackServiceWrapper mServiceWrapper;

    public OnPreviousClickListener(PlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onClick(View v) {
        long GO_TO_PREV_TRACK_THRESHOLD_IN_MILLIS = 2000;
        if (mServiceWrapper.getPlayingPosition() < GO_TO_PREV_TRACK_THRESHOLD_IN_MILLIS) {
            mServiceWrapper.prev();
        } else {
            mServiceWrapper.seek(0);
            mServiceWrapper.play();
        }
    }
}
