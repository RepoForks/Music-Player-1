package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.slidingup_panel.ViewDragHelper;

public class OnPlayPauseClickedListener implements View.OnClickListener{

    public interface Callbacks {
        public void play();
        public void pause();
        public boolean isPlaying();
        public void setPlayPaseButtonsImage();
    }

    private Callbacks mCallbacks;

    public OnPlayPauseClickedListener(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View v) {
        playPause();
        mCallbacks.setPlayPaseButtonsImage();
    }

    private void playPause() {
        if(mCallbacks.isPlaying()) {
            mCallbacks.pause();
        } else {
            mCallbacks.play();
        }
    }
}
