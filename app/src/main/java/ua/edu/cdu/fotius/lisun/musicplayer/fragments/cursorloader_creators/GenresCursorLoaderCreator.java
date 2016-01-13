package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class GenresCursorLoaderCreator extends AbstractCursorLoaderCreator{

    public GenresCursorLoaderCreator(Context context) {
        super(context);
    }

    @Override
    public int getLoaderId() {
        return GENRES_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[] {
            getGenreIdColumnName(),
            getGenreColumnName()
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
        return getGenreColumnName() + " ASC";
    }

    public String getGenreIdColumnName() {
        return AudioStorage.Genres.GENRE_ID;
    }

    public String getGenreColumnName() {
        return AudioStorage.Genres.GENRE;
    }
}
