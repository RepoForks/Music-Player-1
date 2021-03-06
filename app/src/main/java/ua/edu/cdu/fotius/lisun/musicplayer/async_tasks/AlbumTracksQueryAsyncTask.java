package ua.edu.cdu.fotius.lisun.musicplayer.async_tasks;

import android.app.Fragment;
import android.content.Context;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public class AlbumTracksQueryAsyncTask extends BaseTracksQueryAsyncTask {

    private long mArtistID;

    public AlbumTracksQueryAsyncTask(Fragment fragment, Callbacks callbacks, long artistID) {
        super(fragment, callbacks);
        mArtistID = artistID;
    }

    @Override
    protected long[] queryProvider(Context context, long[] albumIDs) {
        return DatabaseUtils.queryAlbumsTracks(context, mArtistID, albumIDs);
    }
}
