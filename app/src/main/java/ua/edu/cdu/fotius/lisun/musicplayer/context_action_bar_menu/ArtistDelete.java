package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class ArtistDelete extends Delete implements BaseTracksQueryAsyncTask.Callbacks{

    public ArtistDelete(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper) {
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
