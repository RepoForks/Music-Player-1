package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;
import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public abstract class Command {

    protected Fragment mFragment;
    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public Command(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        mFragment = fragment;
        mServiceWrapper = serviceWrapper;
    }

    public Context getContext() {
        return mFragment.getActivity();
    }

    public abstract void execute(long[] idsOverWhichToExecute);
}
