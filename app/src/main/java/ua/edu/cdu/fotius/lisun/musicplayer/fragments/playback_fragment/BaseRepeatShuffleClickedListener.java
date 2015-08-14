package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.os.RemoteException;
import android.view.View;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;

public abstract class BaseRepeatShuffleClickedListener implements View.OnClickListener{

    protected ListenerCallbacks mCallbacks;

    public BaseRepeatShuffleClickedListener(ListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onClick(View clickedView) {
        setMode(clickedView);
    }

    public abstract void setMode(View clickedView);
}
