package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;


import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class PlaylistCursorLoaderCreator extends AbstractCursorLoaderCreator {

    public PlaylistCursorLoaderCreator(Context context) {
        super(context);
    }

    @Override
    public int getLoaderId() {
        return PLAYLISTS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return  new String[] {
                getPlaylistIdColumnName(),
                getPlaylistColumnName()
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
        return AudioStorage.Playlist.SORT_ORDER;
    }

    public String getPlaylistIdColumnName() {
        return AudioStorage.Playlist.PLAYLIST_ID;
    }

    public String getPlaylistColumnName() {
        return AudioStorage.Playlist.PLAYLIST;
    }
}
