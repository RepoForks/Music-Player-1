package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;


public class Delete extends Command {

    public Delete(Fragment fragment, PlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new DeleteDialog(mFragment, idsOverWhichToExecute, mServiceWrapper).show();
    }
}
