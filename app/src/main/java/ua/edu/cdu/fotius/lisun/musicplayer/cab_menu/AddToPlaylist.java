package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;


import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.dialogs.ChoosePlaylistDialogFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistsQueryAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.StringConstants;

public class AddToPlaylist extends Command {

    public AddToPlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] tracksID) {
        ChoosePlaylistDialogFragment playlistDialogFragment =
                ChoosePlaylistDialogFragment.newInstance(tracksID);
        playlistDialogFragment.show(mFragment.getActivity().getFragmentManager(),
                StringConstants.DIALOG_FRAGMENT_TAG);
    }
}
