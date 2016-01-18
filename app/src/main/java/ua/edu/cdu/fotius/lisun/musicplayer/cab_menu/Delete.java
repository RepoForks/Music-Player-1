package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;


public class Delete extends Command {

    public Delete(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new DeleteDialog(mFragment, idsOverWhichToExecute, mServiceWrapper).show();
    }
}
