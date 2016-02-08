package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncRetriever;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.RenamePlaylistDialog;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.StringConstants;

public class RenamePlaylist extends Command{
    public RenamePlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        long playlistId = ids[0];
        RenamePlaylistDialog dialog = RenamePlaylistDialog.newInstance(playlistId);
        dialog.show(mFragment.getActivity().getFragmentManager(), StringConstants.DIALOG_FRAGMENT_TAG);
    }
}
