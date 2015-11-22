package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.net.Uri;

public abstract class AbstractAlbumCursorLoaderFactory extends AbstractCursorLoaderFactory{

    protected AbstractAlbumCursorLoaderFactory(Context context) {
        super(context);
    }

    public abstract String getAlbumIdColumnName();
}
