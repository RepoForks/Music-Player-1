package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.dialogs.DeleteTrackDialogFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.StringConstants;


public class Delete extends Command {

    public Delete(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        DeleteTrackDialogFragment fragment = new DeleteTrackDialogFragment();
        DeleteTrackDialogFragment.prepareFragment(fragment, ids);
        fragment.show(mFragment.getFragmentManager(), StringConstants.DIALOG_FRAGMENT_TAG);
    }
}
