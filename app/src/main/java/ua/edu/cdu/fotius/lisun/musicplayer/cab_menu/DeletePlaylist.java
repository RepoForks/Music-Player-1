package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;
import ua.edu.cdu.fotius.lisun.musicplayer.dialogs.DeletePlaylistDialogFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.StringConstants;

public class DeletePlaylist extends Command{

    public DeletePlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        DeletePlaylistDialogFragment fragment = new DeletePlaylistDialogFragment();
        DeletePlaylistDialogFragment.prepareFragment(fragment, ids);
        fragment.show(mFragment.getActivity().getFragmentManager(),
                StringConstants.DIALOG_FRAGMENT_TAG);
    }
}
