package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public class RemoveFromPlaylist extends Command{

    private long mPlaylistId;

    public RemoveFromPlaylist(Fragment fragment, PlaybackServiceWrapper serviceWrapper, long playlistId) {
        super(fragment, serviceWrapper);
        mPlaylistId = playlistId;
    }

    @Override
    public void execute(long[] idsOverWhichToExecute) {
        new RemoveFromPlaylistAsyncTask(mFragment, mPlaylistId, idsOverWhichToExecute)
                .execute();
    }
}
