package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.PlaylistNameAsyncRetriever;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class RenamePlaylist extends Command{
    public RenamePlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] ids) {
        long playlistId = ids[0];
        PlaylistNameAsyncRetriever task = new PlaylistNameAsyncRetriever(mFragment, playlistId);
        task.execute();
    }
}
