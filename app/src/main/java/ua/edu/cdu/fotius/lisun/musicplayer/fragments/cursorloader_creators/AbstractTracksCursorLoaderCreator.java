package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;


import android.content.Context;

public abstract class AbstractTracksCursorLoaderCreator extends AbstractCursorLoaderCreator {

    protected AbstractTracksCursorLoaderCreator(Context context) {
        super(context);
    }

    public abstract String getTrackIdColumnName();
    public abstract String getTrackColumnName();
    public abstract String getArtistColumnName();
    public abstract String getAlbumIdColumnName();
}
