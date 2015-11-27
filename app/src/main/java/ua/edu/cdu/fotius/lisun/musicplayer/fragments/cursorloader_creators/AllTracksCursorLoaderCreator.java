package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class AllTracksCursorLoaderCreator extends AbstractTracksCursorLoaderCreator {

    public AllTracksCursorLoaderCreator(Context context) {
        super(context);
    }

    @Override
    public int getLoaderId() {
        return TRACKS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getTrackIdColumnName(),
                getTrackColumnName(),
                getArtistColumnName(),
                getAlbumIdColumnName()
        };
    }

    @Override
    public String getSelection() {
        return getTrackColumnName() + " != '' AND " + AudioStorage.Track.IS_MUSIC + " = 1";
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

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.Track.ALBUM_ID;
    }
}
