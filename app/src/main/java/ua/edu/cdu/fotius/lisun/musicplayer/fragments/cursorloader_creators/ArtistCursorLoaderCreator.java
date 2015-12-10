package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ArtistCursorLoaderCreator extends AbstractCursorLoaderCreator {

    public ArtistCursorLoaderCreator(Context context) {
        super(context);
    }

    @Override
    public int getLoaderId() {
        return ARTISTS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getArtistIdColumnName(),
                getArtistColumnName(),
                getAlbumsQuantityColumnName(),
                getTracksQuantityColumnName()
        };
    }

    @Override
    public String getSelection() {
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }

    @Override
    public String getSortOrder() {
        return AudioStorage.Artist.SORT_ORDER;
    }

    public String getArtistIdColumnName() {
        return AudioStorage.Artist.ARTIST_ID;
    }

    public String getArtistColumnName() {
        return AudioStorage.Artist.ARTIST;
    }

    public String getAlbumsQuantityColumnName() {
        return AudioStorage.Artist.ALBUMS_QUANTITY;
    }

    public String getTracksQuantityColumnName() {
        return AudioStorage.Artist.TRACKS_QUANTITY;
    }
}
