package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class ArtistAddToPlayQueue extends AddToPlayQueue implements BaseTracksQueryAsyncTask.Callbacks{

    public ArtistAddToPlayQueue(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] artistsID) {
        BaseTracksQueryAsyncTask query =
                new ArtistTracksQueryAsyncTask(mContext, this);
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
