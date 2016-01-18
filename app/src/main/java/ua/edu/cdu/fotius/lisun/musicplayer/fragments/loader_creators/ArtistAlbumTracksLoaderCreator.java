package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class ArtistAlbumTracksLoaderCreator extends AlbumTracksLoaderCreator {

    private long mArtistId;

    public ArtistAlbumTracksLoaderCreator(Context context, long artistId, long albumId) {
        super(context, albumId);
        mArtistId = artistId;
    }

    @Override
    public int getLoaderId() {
        return ARTIST_ALBUM_TRACKS_LOADER_ID;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getTrackIdColumn(),
                getTrackColumn(),
                getArtistColumn(),
                getArtistIdColumn(),
                getAlbumIdColumn()
        };
    }

    public String getArtistIdColumn() {
        return AudioStorage.Track.ARTIST_ID;
    }

    @Override
    public String getSelection() {
        return getAlbumIdColumn() + " = ? AND " + getArtistIdColumn() + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{ Long.toString(mAlbumId), Long.toString(mArtistId)};
    }
}
