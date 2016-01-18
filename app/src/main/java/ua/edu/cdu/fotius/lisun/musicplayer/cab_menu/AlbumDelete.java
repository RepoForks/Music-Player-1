package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AlbumTracksQueryAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.BaseTracksQueryAsyncTask;

public class AlbumDelete extends Delete implements BaseTracksQueryAsyncTask.Callbacks{

    private long mArtistID;

    public AlbumDelete(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long artistID) {
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
