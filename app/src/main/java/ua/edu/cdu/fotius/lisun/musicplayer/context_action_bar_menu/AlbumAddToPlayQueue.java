package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.support.v4.app.Fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.PlaybackServiceWrapper;

public class AlbumAddToPlayQueue extends AddToPlayQueue implements BaseTracksQueryAsyncTask.Callbacks{

    private long mArtistID;

    public AlbumAddToPlayQueue(Fragment fragment, PlaybackServiceWrapper serviceWrapper, long artistID) {
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
