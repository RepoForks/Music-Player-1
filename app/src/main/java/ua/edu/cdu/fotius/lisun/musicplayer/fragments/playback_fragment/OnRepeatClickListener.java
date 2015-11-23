package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.Playlist;

public class OnRepeatClickListener extends BaseRepeatShuffleClickListener {

    public OnRepeatClickListener(MediaPlaybackServiceWrapper serviceWrapper, PlaybackViewsStateListener playbackViewsStateListener) {
        super(serviceWrapper, playbackViewsStateListener);
    }

    @Override
    public void setMode(View clickedView) {
        setRepeatMode(clickedView);
    }

    private void setRepeatMode(View clickedView) {
        int mode = mServiceWrapper.getRepeatMode();
        if (mode != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            if (mode == Playlist.REPEAT_NONE) {
                mServiceWrapper.setRepeatMode(Playlist.REPEAT_ALL);
            } else if (mode == Playlist.REPEAT_ALL) {
                mServiceWrapper.setRepeatMode(Playlist.REPEAT_CURRENT);
                if (mServiceWrapper.getShuffleMode() != Playlist.SHUFFLE_NONE) {
                    mServiceWrapper.setShuffleMode(Playlist.SHUFFLE_NONE);
                    mPlaybackViewsStateListener.updateShuffleButtonImage();
                }
            } else {
                mServiceWrapper.setRepeatMode(Playlist.REPEAT_NONE);
            }
            ((RepeatButton)clickedView).setImage(mServiceWrapper.getRepeatMode());
        }
    }
}
