package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public class ArtistCursorLoaderFactory extends AbstractCursorLoaderFactory{

    protected ArtistCursorLoaderFactory(Context context) {
        super(context);
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                AudioStorage.Artist.ARTIST_ID,
                AudioStorage.Artist.ARTIST,
                AudioStorage.Artist.ALBUMS_QUANTITY,
                AudioStorage.Artist.TRACKS_QUANTITY
        };
    }

    @Override
    public String getSelection() {
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }

    @Override
    public String getSortOrder() {
        return AudioStorage.Artist.SORT_ORDER;
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
