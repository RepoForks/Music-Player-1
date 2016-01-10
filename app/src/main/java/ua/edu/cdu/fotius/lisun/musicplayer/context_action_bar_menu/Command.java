package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

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
