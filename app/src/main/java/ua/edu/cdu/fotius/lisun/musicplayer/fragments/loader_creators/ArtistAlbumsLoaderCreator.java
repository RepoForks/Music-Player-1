package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class ArtistAlbumsLoaderCreator extends BaseAlbumsLoaderCreator {

    private long mArtistId;

    public ArtistAlbumsLoaderCreator(Context context, long artistId) {
        super(context);
        mArtistId = artistId;
    }

    @Override
    public int getLoaderId() {
        return ARTIST_ALBUMS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[] {
                getAlbumIdColumn(),
                getTrackIdColumn(),
                getAlbumColumn(),
                getArtistColumn()
        };
    }

    @Override
    public String getSelection() {
        return getArtistIdColumn() + " = ?) GROUP BY ( " + getAlbumIdColumn();
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{Long.toString(mArtistId)};
    }

    @Override
    public String getSortOrder() {
        return null;
    }

    public String getTrackIdColumn() {
        return AudioStorage.Track.TRACK_ID;
    }

    @Override
    public String getAlbumIdColumn() {
        return AudioStorage.Track.ALBUM_ID;
    }

    @Override
    public String getAlbumColumn() {
        return AudioStorage.Track.ALBUM;
    }

    public String getArtistIdColumn() {
        return AudioStorage.Track.ARTIST_ID;
    }

    @Override
    public String getArtistColumn() {
        return AudioStorage.Track.ARTIST;
    }
}
