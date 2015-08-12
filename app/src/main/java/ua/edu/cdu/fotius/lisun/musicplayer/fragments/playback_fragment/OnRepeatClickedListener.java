package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;

public class OnRepeatClickedListener extends BaseRepeatShuffleClickedListener{

    public OnRepeatClickedListener(Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void setMode(View clickedView) {
        setRepeatMode();
    }

    private void setRepeatMode() {
        int mode = mCallbacks.getRepeatMode();
        if (mode != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            if (mode == Playlist.REPEAT_NONE) {
                mCallbacks.setRepeatMode(Playlist.REPEAT_ALL);
            } else if (mode == Playlist.REPEAT_ALL) {
                mCallbacks.setRepeatMode(Playlist.REPEAT_CURRENT);
                if (mCallbacks.getShuffleMode() != Playlist.SHUFFLE_NONE) {
                    mCallbacks.setShuffleMode(Playlist.SHUFFLE_NONE);
                    mCallbacks.setShuffleButtonImage();
                }
            } else {
                mCallbacks.setRepeatMode(Playlist.REPEAT_NONE);
            }
            mCallbacks.setRepeatButtonImage();
        }
    }
}
