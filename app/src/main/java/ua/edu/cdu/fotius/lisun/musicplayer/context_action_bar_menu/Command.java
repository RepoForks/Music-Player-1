package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public abstract class Command {

    protected Context mContext;
    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public Command(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        mContext = context;
        mServiceWrapper = serviceWrapper;
    }

    public abstract void execute(long[] idsOverWhichToExecute);
}
