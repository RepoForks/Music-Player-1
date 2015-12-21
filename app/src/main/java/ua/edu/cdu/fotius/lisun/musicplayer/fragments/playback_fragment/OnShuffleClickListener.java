package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ShuffleButton;
import ua.edu.cdu.fotius.lisun.musicplayer.Playlist;

public class OnShuffleClickListener extends BaseRepeatShuffleClickListener {

    public interface ShuffleClickedListener {
        public void onShuffleClicked();
    }

    private ShuffleClickedListener mClickedListener;

    public OnShuffleClickListener(MediaPlaybackServiceWrapper serviceWrapper,
                                  ShuffleClickedListener clickedListener) {
        super(serviceWrapper);
        mClickedListener = clickedListener;
    }

    @Override
    public void setMode(View clickedView) {
        setShuffleMode(clickedView);
    }

    private void setShuffleMode(View clickedView) {
        int shuffle =  mServiceWrapper.getShuffleMode();
        if (shuffle != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            /*As we entered this method we clicked shuffle button for specific album
            * so only two shuffle mode available. "Party shuffle" isn't available in this case*/
            //TODO: add party shuffle in toolbar menu
            if (shuffle == Playlist.SHUFFLE_NONE) {
                mServiceWrapper.setShuffleMode(Playlist.SHUFFLE_NORMAL);
                if (mServiceWrapper.getRepeatMode() == Playlist.REPEAT_CURRENT) {
                    mServiceWrapper.setRepeatMode(Playlist.REPEAT_ALL);
                    mClickedListener.onShuffleClicked();
                }
            } else if (shuffle == Playlist.SHUFFLE_NORMAL ||
                    shuffle == Playlist.SHUFFLE_AUTO) {
                mServiceWrapper.setShuffleMode(Playlist.SHUFFLE_NONE);
            }
            ((ShuffleButton)clickedView).setImage(mServiceWrapper.getShuffleMode());
        }
    }
}
