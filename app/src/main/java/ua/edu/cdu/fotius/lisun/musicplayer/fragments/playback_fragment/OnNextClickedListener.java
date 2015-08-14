package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

public class OnNextClickedListener implements View.OnClickListener {

    private ListenerCallbacks mCallbacks;

    public OnNextClickedListener(ListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        mCallbacks.goToNextTrack();
    }
}
