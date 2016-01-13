package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public class AddToPlayQueue extends Command {

    public AddToPlayQueue(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        mServiceWrapper.addToTheEndOfPlayingQueue(idsOverWhichToExecute);
    }
}
