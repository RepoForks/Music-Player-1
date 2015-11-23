package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ArtistAlbumsCursorLoaderCreator extends AbstractAlbumCursorLoaderCreator {

    private long mArtistId;

    public ArtistAlbumsCursorLoaderCreator(Context context, long artistId) {
        super(context);
        mArtistId = artistId;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[] {
                getTrackIdColumnName(),
                getAlbumIdColumnName(),
                getAlbumColumnName(),
                getArtistColumnName()
        };
    }

    @Override
    public String getSelection() {
        return getArtistIdColumnName() + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{Long.toString(mArtistId)};
    }

    @Override
    public String getSortOrder() {
        return null;
    }

    public String getTrackIdColumnName() {
        return AudioStorage.Track.TRACK_ID;
    }

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.Track.ALBUM_ID;
    }

    @Override
    public String getAlbumColumnName() {
        return AudioStorage.Track.ALBUM;
    }

    public String getArtistIdColumnName() {
        return AudioStorage.Track.ARTIST_ID;
    }

    @Override
    public String getArtistColumnName() {
        return AudioStorage.Track.ARTIST;
    }
}
