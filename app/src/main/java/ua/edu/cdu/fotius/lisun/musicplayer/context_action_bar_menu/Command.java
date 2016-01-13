package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public abstract class Command {

    protected Fragment mFragment;
    protected PlaybackServiceWrapper mServiceWrapper;

    public Command(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        mFragment = fragment;
        mServiceWrapper = serviceWrapper;
    }

    public Context getContext() {
        return mFragment.getActivity();
    }

    public abstract void execute(long[] idsOverWhichToExecute);
}
