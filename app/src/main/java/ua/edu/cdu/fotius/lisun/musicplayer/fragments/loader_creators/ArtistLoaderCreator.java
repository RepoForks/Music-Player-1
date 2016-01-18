package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class ArtistLoaderCreator extends BaseLoaderCreator {

    public ArtistLoaderCreator(Context context) {
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
                getArtistIdColumn(),
                getArtistColumn(),
                getAlbumsQuantityColumn(),
                getTracksQuantityColumn()
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

    public String getArtistIdColumn() {
        return AudioStorage.Artist.ARTIST_ID;
    }

    public String getArtistColumn() {
        return AudioStorage.Artist.ARTIST;
    }

    public String getAlbumsQuantityColumn() {
        return AudioStorage.Artist.ALBUMS_QUANTITY;
    }

    public String getTracksQuantityColumn() {
        return AudioStorage.Artist.TRACKS_QUANTITY;
    }
}
