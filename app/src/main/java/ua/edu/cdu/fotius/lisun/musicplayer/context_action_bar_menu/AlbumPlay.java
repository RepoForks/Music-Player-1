package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;
import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AlbumPlay extends Play implements BaseTracksQueryAsyncTask.Callbacks{

    private long mArtistID;

    public AlbumPlay(Fragment fragment, MediaPlaybackServiceWrapper serviceWrapper, long artistID) {
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
    public void onQueryCompleted(long[] trackIds) {
        if(trackIds != null) {
            super.execute(trackIds);
        }
    }

}
