package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class AlbumsCursorLoaderCreator extends AbstractAlbumCursorLoaderCreator {

    public AlbumsCursorLoaderCreator(Context context) {
        super(context);
    }

    @Override
    public int getLoaderId() {
        return ALBUMS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[] {
                getAlbumIdColumnName(),
                getAlbumColumnName(),
                getArtistColumnName()
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
        return  AudioStorage.Album.SORT_ORDER;
    }

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.Album.ALBUM_ID;
    }

    @Override
    public String getAlbumColumnName() {
        return AudioStorage.Album.ALBUM;
    }

    @Override
    public String getArtistColumnName() {
        return AudioStorage.Album.ARTIST;
    }

}
