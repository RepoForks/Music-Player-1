package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistsQueryAsyncTask;

public class AddToPlaylist extends Command {

    public AddToPlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] tracksID) {
        new PlaylistsQueryAsyncTask(mFragment, tracksID).execute();
    }
}
