package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class GenreTracksCursorLoaderCreator extends AbstractTracksCursorLoaderCreator{

    private long mGenreId;

    public GenreTracksCursorLoaderCreator(Context context, long genreId) {
        super(context);
        mGenreId = genreId;
    }

    @Override
    public int getLoaderId() {
        return GENRE_TRACKS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Genres.Members.getContentUri("external", mGenreId);
    }

    @Override
    public String[] getProjection() {
        return new String[] {
                getPrimaryIdColumnName(),
                getTrackIdColumnName(),
                getTrackColumnName(),
                getArtistColumnName(),
                getAlbumIdColumnName()
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
        return AudioStorage.GenreMember.SORT_ORDER;
    }

    public String getPrimaryIdColumnName() {
        return AudioStorage.GenreMember.PRIMARY_ID;
    }

    @Override
    public String getTrackIdColumnName() {
        return AudioStorage.GenreMember.TRACK_ID;
    }

    @Override
    public String getTrackColumnName() {
        return AudioStorage.GenreMember.TRACK;
    }

    @Override
    public String getArtistColumnName() {
        return AudioStorage.GenreMember.ARTIST;
    }

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.GenreMember.ALBUM_ID;
    }
}
