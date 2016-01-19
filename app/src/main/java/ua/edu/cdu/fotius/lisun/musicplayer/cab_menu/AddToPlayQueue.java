package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class AddToPlayQueue extends Command {

    public AddToPlayQueue(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        mServiceWrapper.addToTheEndOfPlayQueue(idsOverWhichToExecute);
    }
}