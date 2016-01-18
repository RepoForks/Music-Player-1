package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;


import android.content.Context;

public abstract class BaseTracksLoaderCreator extends BaseLoaderCreator {

    protected BaseTracksLoaderCreator(Context context) {
        super(context);
    }

    public abstract String getTrackIdColumn();
    public abstract String getTrackColumn();
    public abstract String getArtistColumn();
    public abstract String getAlbumIdColumn();
}
