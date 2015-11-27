package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

public abstract class AbstractCursorLoaderCreator {

    private final String TAG = getClass().getSimpleName();

    /*Need to declare all children's loaders here,
    because will be more convenient when will adding new child*/
    protected static final int TRACKS_LOADER_ID = 1;
    protected static final int ALBUM_TRACKS_LOADER_ID = 2;
    protected static final int ARTIST_ALBUM_TRACKS_LOADER_ID = 3;
    protected static final int PLAYLIST_TRACKS_LOADER_ID = 4;
    protected static final int PLAYLISTS_LOADER_ID = 5;
    protected static final int ARTISTS_LOADER_ID = 6;
    protected static final int ALBUMS_LOADER_ID = 7;
    protected static final int ARTIST_ALBUMS_LOADER_ID = 8;

    private Context mContext;

    protected AbstractCursorLoaderCreator(Context context) {
        mContext = context;
    }

    public CursorLoader createCursorLoader() {
        DatabaseUtils.queryParamsInLog(getUri(), getProjection(), getSelection(), getSelectionArgs());

        return new CursorLoader(mContext, getUri(), getProjection(),
                getSelection(), getSelectionArgs(), getSortOrder());
    }

    public abstract int getLoaderId();
    public abstract Uri getUri();
    public abstract String[] getProjection();
    public abstract String getSelection();
    public abstract String[] getSelectionArgs();
    public abstract String getSortOrder();
}
