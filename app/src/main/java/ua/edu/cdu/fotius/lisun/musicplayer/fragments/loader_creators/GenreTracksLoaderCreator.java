package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class GenreTracksLoaderCreator extends BaseTracksLoaderCreator {

    private long mGenreId;

    public GenreTracksLoaderCreator(Context context, long genreId) {
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
                getPrimaryIdColumn(),
                getTrackIdColumn(),
                getTrackColumn(),
                getArtistColumn(),
                getAlbumIdColumn()
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

    public String getPrimaryIdColumn() {
        return AudioStorage.GenreMember.PRIMARY_ID;
    }

    @Override
    public String getTrackIdColumn() {
        return AudioStorage.GenreMember.TRACK_ID;
    }

    @Override
    public String getTrackColumn() {
        return AudioStorage.GenreMember.TRACK;
    }

    @Override
    public String getArtistColumn() {
        return AudioStorage.GenreMember.ARTIST;
    }

    @Override
    public String getAlbumIdColumn() {
        return AudioStorage.GenreMember.ALBUM_ID;
    }
}
