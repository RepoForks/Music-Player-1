package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ArtistAlbumsCursorLoaderFactory extends AbstractAlbumCursorLoaderFactory {

    private long mArtistId;

    public ArtistAlbumsCursorLoaderFactory(Context context, long artistId) {
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
                AudioStorage.Track.TRACK_ID,
                AudioStorage.Track.ALBUM_ID,
                AudioStorage.Track.ALBUM,
                AudioStorage.Track.ARTIST
        };
    }

    @Override
    public String getSelection() {
        return AudioStorage.Track.ARTIST_ID + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{Long.toString(mArtistId)};
    }

    @Override
    public String getSortOrder() {
        return null;
    }

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.Track.ALBUM_ID;
    }
}
