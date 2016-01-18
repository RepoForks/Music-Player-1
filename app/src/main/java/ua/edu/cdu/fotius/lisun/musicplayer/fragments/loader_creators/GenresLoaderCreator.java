package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class GenresLoaderCreator extends BaseLoaderCreator {

    public GenresLoaderCreator(Context context) {
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
            getGenreIdColumn(),
            getGenreColumn()
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
        return getGenreColumn() + " ASC";
    }

    public String getGenreIdColumn() {
        return AudioStorage.Genres.GENRE_ID;
    }

    public String getGenreColumn() {
        return AudioStorage.Genres.GENRE;
    }
}
