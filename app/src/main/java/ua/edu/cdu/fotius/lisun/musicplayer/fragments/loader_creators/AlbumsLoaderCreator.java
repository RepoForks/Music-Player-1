package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class AlbumsLoaderCreator extends BaseAlbumsLoaderCreator {

    public AlbumsLoaderCreator(Context context) {
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
                getAlbumIdColumn(),
                getAlbumColumn(),
                getArtistColumn()
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
    public String getAlbumIdColumn() {
        return AudioStorage.Album.ALBUM_ID;
    }

    @Override
    public String getAlbumColumn() {
        return AudioStorage.Album.ALBUM;
    }

    @Override
    public String getArtistColumn() {
        return AudioStorage.Album.ARTIST;
    }

}
