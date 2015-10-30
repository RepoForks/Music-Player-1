package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ArtistAlbumsCursorLoaderFactory extends AlbumsCursorLoaderFactory {

    private long mArtistId;

    public ArtistAlbumsCursorLoaderFactory(Context context, long artistId) {
        super(context);
        mArtistId = artistId;
    }

    @Override
    public String getSelection() {
        return AudioStorage.Album.ARTIST_ID + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{Long.toString(mArtistId)};
    }
}
