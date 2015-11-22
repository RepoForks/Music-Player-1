package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ArtistAlbumTracksCursorLoaderFactory extends AlbumTracksCursorLoaderFactory {

    private long mArtistId;

    protected ArtistAlbumTracksCursorLoaderFactory(Context context, long artistId, long albumId) {
        super(context, albumId);
        mArtistId = artistId;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                AudioStorage.Track.TRACK_ID,
                AudioStorage.Track.TRACK,
                AudioStorage.Track.ARTIST,
                AudioStorage.Track.ARTIST_ID,
                AudioStorage.Track.ALBUM_ID
        };
    }

    @Override
    public String getSelection() {
        return AudioStorage.Track.ALBUM_ID + " = ? AND " + AudioStorage.Track.ARTIST_ID + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{ Long.toString(mAlbumId), Long.toString(mArtistId)};
    }
}
