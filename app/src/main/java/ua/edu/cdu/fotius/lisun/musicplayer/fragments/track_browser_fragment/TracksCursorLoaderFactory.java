package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.content.Context;

public abstract class TracksCursorLoaderFactory extends AbstractCursorLoaderFactory{

    protected TracksCursorLoaderFactory(Context context) {
        super(context);
    }

    public abstract String getTrackIdColumnName();
    public abstract String getTrackColumnName();
    public abstract String getArtistColumnName();
}
