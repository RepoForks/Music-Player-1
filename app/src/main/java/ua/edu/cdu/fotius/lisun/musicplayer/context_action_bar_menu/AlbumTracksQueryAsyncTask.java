package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class AlbumTracksQueryAsyncTask extends BaseTracksQueryAsyncTask{

    public AlbumTracksQueryAsyncTask(Context context, Callbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    protected long[] queryProvider(Context context, long[] albumIDs) {
        return DatabaseUtils.queryAlbumsTracks(context, albumIDs);
    }
}
