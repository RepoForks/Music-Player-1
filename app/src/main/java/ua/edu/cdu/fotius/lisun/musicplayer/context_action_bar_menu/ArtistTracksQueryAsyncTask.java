package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class ArtistTracksQueryAsyncTask extends BaseTracksQueryAsyncTask{

    public ArtistTracksQueryAsyncTask(Context context, Callbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    protected long[] queryProvider(Context context, long[] artistIDs) {
        return DatabaseUtils.queryArtistsTracks(context, artistIDs);
    }
}
