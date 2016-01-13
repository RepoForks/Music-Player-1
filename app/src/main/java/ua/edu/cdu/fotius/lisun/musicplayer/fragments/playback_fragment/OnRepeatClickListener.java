package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.Playlist;

public class OnRepeatClickListener extends BaseRepeatShuffleClickListener {

    public interface RepeatClickedListener {
        public void onRepeatClicked();
    }

    private RepeatClickedListener mClickedListener;

    public OnRepeatClickListener(PlaybackServiceWrapper serviceWrapper,
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
        if (mode != PlaybackServiceWrapper.ERROR_RETURN_VALUE) {
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
