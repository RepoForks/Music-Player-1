package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import java.util.ArrayList;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public abstract class Command {

    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public Command(MediaPlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    public abstract void execute(long[] idsOverWhichToExecute);
}
