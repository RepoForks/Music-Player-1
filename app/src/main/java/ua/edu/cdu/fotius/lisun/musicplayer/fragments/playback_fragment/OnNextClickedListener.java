package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

public class OnNextClickedListener implements View.OnClickListener {

    interface OnNextClickedListenerCallbacks {
        public void goToNextTrack();
    }

    OnNextClickedListenerCallbacks mCallbacks;

    public OnNextClickedListener(OnNextClickedListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        mCallbacks.goToNextTrack();
    }
}
