package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class PlayAlbum extends Play implements BaseTracksQueryAsyncTask.Callbacks{

    public PlayAlbum(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] albumsID) {
        BaseTracksQueryAsyncTask query =
                new AlbumTracksQueryAsyncTask(getContext(), this);
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
