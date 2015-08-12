package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.os.RemoteException;
import android.view.View;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;

public abstract class BaseRepeatShuffleClickedListener implements View.OnClickListener{

    public interface Callbacks {
        public void setRepeatMode(int repeatMode);
        public int getRepeatMode();

        public void setShuffleMode(int shuffleMode);
        public int getShuffleMode();

        public void setRepeatButtonImage();
        public void setShuffleButtonImage();
    }

    protected Callbacks mCallbacks;

    public BaseRepeatShuffleClickedListener(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View clickedView) {
        setMode(clickedView);
    }

    public abstract void setMode(View clickedView);
}
