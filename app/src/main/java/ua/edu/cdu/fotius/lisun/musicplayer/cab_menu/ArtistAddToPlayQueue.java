package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.ArtistTracksQueryAsyncTask;
import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.BaseTracksQueryAsyncTask;

public class ArtistAddToPlayQueue extends AddToPlayQueue implements BaseTracksQueryAsyncTask.Callbacks{

    public ArtistAddToPlayQueue(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
        super(fragment, serviceWrapper);
    }

    @Override
    public void execute(long[] artistsID) {
        BaseTracksQueryAsyncTask query =
                new ArtistTracksQueryAsyncTask(mFragment, this);
        query.execute(artistsID);
    }

    @Override
    public void onQueryStart() {

    }

    @Override
    public void onQueryCompleted(long[] trackIds) {
        if(trackIds != null) {
            super.execute(trackIds);
        }
    }
}
