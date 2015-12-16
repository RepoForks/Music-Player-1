package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class ArtistPlay extends Play implements BaseTracksQueryAsyncTask.Callbacks{

    public ArtistPlay(Context context, MediaPlaybackServiceWrapper serviceWrapper) {
        super(context, serviceWrapper);
    }

    @Override
    public void execute(long[] artistsID) {
        BaseTracksQueryAsyncTask query =
                new ArtistTracksQueryAsyncTask(getContext(), this);
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
