package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.Context;


import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class ArtistTracksQueryAsyncTask extends BaseTracksQueryAsyncTask {

    public ArtistTracksQueryAsyncTask(Fragment fragment, Callbacks callbacks) {
        super(fragment, callbacks);
    }

    @Override
    protected long[] queryProvider(Context context, long[] artistIDs) {
        return DatabaseUtils.queryArtistsTracks(context, artistIDs);
    }
}
