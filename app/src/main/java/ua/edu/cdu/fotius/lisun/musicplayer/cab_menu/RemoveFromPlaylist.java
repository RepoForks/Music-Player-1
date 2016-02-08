package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.RemoveFromPlaylistAsyncTask;

public class RemoveFromPlaylist extends Command{

    private long mPlaylistId;

    public RemoveFromPlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long playlistId) {
        super(fragment, serviceWrapper);
        mPlaylistId = playlistId;
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new RemoveFromPlaylistAsyncTask(mFragment, mPlaylistId, idsOverWhichToExecute)
                .execute();
    }
}
