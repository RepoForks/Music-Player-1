package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

public class OnPreviousClickedListener implements View.OnClickListener{

    interface OnPreviousClickedListenerCallbacks {
        public void goToPreviousTrack();
        public long getPlayingPosition();
        public void seek(long position);
        public void play();
    }

    OnPreviousClickedListenerCallbacks mCallbacks;

    public OnPreviousClickedListener(OnPreviousClickedListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        long changeTrackThresholdInMillis = 2000;
        if (mCallbacks.getPlayingPosition() < changeTrackThresholdInMillis) {
            mCallbacks.goToPreviousTrack();
        } else {
            mCallbacks.seek(0);
            mCallbacks.play();
        }
    }
}
