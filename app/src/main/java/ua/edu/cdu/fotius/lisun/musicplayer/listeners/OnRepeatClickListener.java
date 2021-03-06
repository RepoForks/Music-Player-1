package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.service.Playlist;

public class OnRepeatClickListener extends BaseRepeatShuffleClickListener {

    public interface RepeatClickedListener {
        public void onRepeatClicked();
    }

    private RepeatClickedListener mClickedListener;

    public OnRepeatClickListener(MediaPlaybackServiceWrapper serviceWrapper,
                                 RepeatClickedListener clickedListener) {
        super(serviceWrapper);
        mClickedListener = clickedListener;
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
                    mClickedListener.onRepeatClicked();
                }
            } else {
                mServiceWrapper.setRepeatMode(Playlist.REPEAT_NONE);
            }
            ((RepeatButton)clickedView).setImage(mServiceWrapper.getRepeatMode());
        }
    }
}
