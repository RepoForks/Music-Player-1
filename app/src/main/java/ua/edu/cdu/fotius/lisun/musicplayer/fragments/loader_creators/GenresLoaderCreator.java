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
        //FIXME: very quick fix. maybe possible to do this in other way
        return " _id in (select genre_id from audio_genres_map where audio_id " +
                "in (select _id from audio_meta where is_music != 0))";
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
