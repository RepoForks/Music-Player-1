package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

public class OnPreviousClickedListener implements View.OnClickListener{

    private ListenerCallbacks mCallbacks;

    public OnPreviousClickedListener(ListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        long GO_TO_PREV_TRACK_THRESHOLD_IN_MILLIS = 2000;
        if (mCallbacks.getPlayingPosition() < GO_TO_PREV_TRACK_THRESHOLD_IN_MILLIS) {
            mCallbacks.goToPreviousTrack();
        } else {
            mCallbacks.seek(0);
            mCallbacks.play();
        }
    }
}
