package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;

public abstract class AbstractAlbumCursorLoaderCreator extends AbstractCursorLoaderCreator {

    protected AbstractAlbumCursorLoaderCreator(Context context) {
        super(context);
    }

    public abstract String getAlbumIdColumnName();
    public abstract String getAlbumColumnName();
    public abstract String getArtistColumnName();
}
