package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

public abstract class BaseTrackCursorLoaderFactory {

    private Context mContext;

    protected BaseTrackCursorLoaderFactory(Context context) {
        mContext = context;
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(mContext, getUri(), getProjection(),
                getSelection(), getSelectionArgs(), getSortOrder());
    }

    public abstract Uri getUri();
    public abstract String[] getProjection();
    public abstract String getSelection();
    public abstract String[] getSelectionArgs();
    public abstract String getSortOrder();
    public abstract String getTrackIdColumnName();
    public abstract String getTrackColumnName();
    public abstract String getArtistColumnName();
}
