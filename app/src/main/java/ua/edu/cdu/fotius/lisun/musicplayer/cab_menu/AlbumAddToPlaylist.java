package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;


import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AlbumTracksQueryAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.BaseTracksQueryAsyncTask;

public class AlbumAddToPlaylist extends AddToPlaylist implements BaseTracksQueryAsyncTask.Callbacks{

    private long mArtistID;

    public AlbumAddToPlaylist(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long artistID) {
        super(fragment, serviceWrapper);
        mArtistID = artistID;
    }

    @Override
    public void execute(long[] albumsID) {
        BaseTracksQueryAsyncTask query =
                new AlbumTracksQueryAsyncTask(mFragment, this, mArtistID);
        query.execute(albumsID);
    }

    @Override
    public void onQueryStart() {
    }

    @Override
    public void onQueryCompleted(long[] tracksId) {
        if(tracksId != null) {
            super.execute(tracksId);
        }
    }
}
