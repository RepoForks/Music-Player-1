package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ArtistAlbumTracksCursorLoaderCreator extends AlbumTracksCursorLoaderCreator {

    private long mArtistId;

    public ArtistAlbumTracksCursorLoaderCreator(Context context, long artistId, long albumId) {
        super(context, albumId);
        mArtistId = artistId;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getTrackIdColumnName(),
                getTrackColumnName(),
                getArtistColumnName(),
                getArtistIdColumnName(),
                getAlbumIdColumnName()
        };
    }

    public String getArtistIdColumnName() {
        return AudioStorage.Track.ARTIST_ID;
    }

    @Override
    public String getSelection() {
        return getAlbumIdColumnName() + " = ? AND " + getArtistIdColumnName() + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{ Long.toString(mAlbumId), Long.toString(mArtistId)};
    }
}
