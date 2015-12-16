package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class AlbumPlay extends Play implements BaseTracksQueryAsyncTask.Callbacks{

    private long mArtistID;

    public AlbumPlay(Context context, MediaPlaybackServiceWrapper serviceWrapper, long artistID) {
        super(context, serviceWrapper);
        mArtistID = artistID;
    }

    @Override
    public void execute(long[] albumsID) {
        BaseTracksQueryAsyncTask query =
                new AlbumTracksQueryAsyncTask(getContext(), this, mArtistID);
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
