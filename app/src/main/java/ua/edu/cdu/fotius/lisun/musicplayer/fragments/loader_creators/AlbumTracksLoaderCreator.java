package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;

import android.content.Context;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class AlbumTracksLoaderCreator extends AllTracksLoaderCreator {

    protected long mAlbumId;

    public AlbumTracksLoaderCreator(Context context, long albumId) {
        super(context);
        mAlbumId = albumId;
    }

    @Override
    public int getLoaderId() {
        return ALBUM_TRACKS_LOADER_ID;
    }

    @Override
    public String getSelection() {
        return AudioStorage.Track.ALBUM_ID + " = ?";
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[]{ Long.toString(mAlbumId)};
    }
}
