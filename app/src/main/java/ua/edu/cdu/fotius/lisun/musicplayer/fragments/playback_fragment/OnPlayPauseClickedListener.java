package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.ViewDragHelper;

public class OnPlayPauseClickedListener implements View.OnClickListener{


    private ListenerCallbacks mCallbacks;

    public OnPlayPauseClickedListener(ListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        playPause();
        mCallbacks.setPlayPauseButtonsImageCallback();
    }

    private void playPause() {
        if(mCallbacks.isPlaying()) {
            mCallbacks.pause();
        } else {
            mCallbacks.play();
        }
    }
}
