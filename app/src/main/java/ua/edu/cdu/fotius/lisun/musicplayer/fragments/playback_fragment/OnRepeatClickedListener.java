package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;

public class OnRepeatClickedListener extends BaseRepeatShuffleClickedListener{

    public OnRepeatClickedListener(ListenerCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void setMode(View clickedView) {
        setRepeatMode(clickedView);
    }

    private void setRepeatMode(View clickedView) {
        int mode = mCallbacks.getRepeatMode();
        if (mode != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            if (mode == Playlist.REPEAT_NONE) {
                mCallbacks.setRepeatMode(Playlist.REPEAT_ALL);
            } else if (mode == Playlist.REPEAT_ALL) {
                mCallbacks.setRepeatMode(Playlist.REPEAT_CURRENT);
                if (mCallbacks.getShuffleMode() != Playlist.SHUFFLE_NONE) {
                    mCallbacks.setShuffleMode(Playlist.SHUFFLE_NONE);
                    mCallbacks.setShuffleButtonImageCallback();
                }
            } else {
                mCallbacks.setRepeatMode(Playlist.REPEAT_NONE);
            }
            ((RepeatButton)clickedView).setImage(mCallbacks.getRepeatMode());
        }
    }
}
