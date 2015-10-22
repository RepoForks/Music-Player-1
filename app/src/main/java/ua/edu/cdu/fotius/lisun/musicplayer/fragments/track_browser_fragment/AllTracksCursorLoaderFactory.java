package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class AllTracksCursorLoaderFactory extends BaseTrackCursorLoaderFactory{

    protected AllTracksCursorLoaderFactory(Context context) {
        super(context);
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                AudioStorage.Track.TRACK_ID,
                AudioStorage.Track.TRACK,
                AudioStorage.Track.ARTIST
        };
    }

    @Override
    public String getSelection() {
        return AudioStorage.Track.TRACK + " != '' AND " + AudioStorage.Track.IS_MUSIC + " = 1";
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }

    @Override
    public String getSortOrder() {
        return AudioStorage.Track.SORT_ORDER;
    }

    @Override
    public String getTrackIdColumnName() {
        return AudioStorage.Track.TRACK_ID;
    }

    @Override
    public String getTrackColumnName() {
        return AudioStorage.Track.TRACK;
    }

    @Override
    public String getArtistColumnName() {
        return AudioStorage.Track.ARTIST;
    }
}
