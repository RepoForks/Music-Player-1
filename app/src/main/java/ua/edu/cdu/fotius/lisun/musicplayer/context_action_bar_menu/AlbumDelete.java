package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AlbumDelete extends Delete implements BaseTracksQueryAsyncTask.Callbacks{

    private long mArtistID;

    public AlbumDelete(Context context, MediaPlaybackServiceWrapper serviceWrapper, long artistID) {
        super(context, serviceWrapper);
        mArtistID = artistID;
    }

    @Override
    public void execute(long[] albumsID) {
        BaseTracksQueryAsyncTask query =
                new AlbumTracksQueryAsyncTask(mContext, this, mArtistID);
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
