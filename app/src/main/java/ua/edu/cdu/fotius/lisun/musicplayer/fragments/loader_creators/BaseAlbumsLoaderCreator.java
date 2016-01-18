package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;

public abstract class BaseAlbumsLoaderCreator extends BaseLoaderCreator {

    protected BaseAlbumsLoaderCreator(Context context) {
        super(context);
    }

    public abstract String getAlbumIdColumn();
    public abstract String getAlbumColumn();
    public abstract String getArtistColumn();
}
