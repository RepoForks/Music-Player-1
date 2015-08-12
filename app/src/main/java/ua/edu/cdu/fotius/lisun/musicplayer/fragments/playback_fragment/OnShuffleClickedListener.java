package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;

public class OnShuffleClickedListener extends BaseRepeatShuffleClickedListener {

    public OnShuffleClickedListener(Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void setMode(View clickedView) {
        setShuffleMode();
    }

    private void setShuffleMode() {
        int shuffle = mCallbacks.getShuffleMode();
        if (shuffle != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            /*As we entered this method we clicked shuffle button for specific album
            * so only two shuffle mode available. "Party shuffle" isn't available in this case*/
            //TODO: add party shuffle in toolbar menu
            if (shuffle == Playlist.SHUFFLE_NONE) {
                mCallbacks.setShuffleMode(Playlist.SHUFFLE_NORMAL);
                if (mCallbacks.getRepeatMode() == Playlist.REPEAT_CURRENT) {
                    mCallbacks.setRepeatMode(Playlist.REPEAT_ALL);
                    mCallbacks.setRepeatButtonImage();
                }
            } else if (shuffle == Playlist.SHUFFLE_NORMAL ||
                    shuffle == Playlist.SHUFFLE_AUTO) {
                mCallbacks.setShuffleMode(Playlist.SHUFFLE_NONE);
            }
            mCallbacks.setShuffleButtonImage();
        }
    }
}
