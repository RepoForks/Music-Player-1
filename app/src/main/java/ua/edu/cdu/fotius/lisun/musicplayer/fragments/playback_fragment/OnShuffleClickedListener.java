package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ShuffleButton;
import ua.edu.cdu.fotius.lisun.musicplayer.Playlist;

public class OnShuffleClickedListener extends BaseRepeatShuffleClickedListener {

    public OnShuffleClickedListener(ListenerCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void setMode(View clickedView) {
        setShuffleMode(clickedView);
    }

    private void setShuffleMode(View clickedView) {
        int shuffle = mCallbacks.getShuffleMode();
        if (shuffle != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            /*As we entered this method we clicked shuffle button for specific album
            * so only two shuffle mode available. "Party shuffle" isn't available in this case*/
            //TODO: add party shuffle in toolbar menu
            if (shuffle == Playlist.SHUFFLE_NONE) {
                mCallbacks.setShuffleMode(Playlist.SHUFFLE_NORMAL);
                if (mCallbacks.getRepeatMode() == Playlist.REPEAT_CURRENT) {
                    mCallbacks.setRepeatMode(Playlist.REPEAT_ALL);
                    mCallbacks.setRepeatButtonImageCallback();
                }
            } else if (shuffle == Playlist.SHUFFLE_NORMAL ||
                    shuffle == Playlist.SHUFFLE_AUTO) {
                mCallbacks.setShuffleMode(Playlist.SHUFFLE_NONE);
            }
            ((ShuffleButton)clickedView).setImage(mCallbacks.getShuffleMode());
        }
    }
}
