package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public class AlbumsCursorLoaderFactory extends AbstractCursorLoaderFactory{

    public AlbumsCursorLoaderFactory(Context context) {
        super(context);
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[] {
                AudioStorage.Album.ALBUM_ID,
                AudioStorage.Album.ALBUM,
                AudioStorage.Album.ARTIST
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
}
