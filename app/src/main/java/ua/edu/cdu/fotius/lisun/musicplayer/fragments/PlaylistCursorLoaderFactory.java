package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment.AbstractCursorLoaderFactory;

public class PlaylistCursorLoaderFactory extends AbstractCursorLoaderFactory{

    public PlaylistCursorLoaderFactory(Context context) {
        super(context);
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return  new String[] {
                AudioStorage.Playlist.PLAYLIST_ID,
                AudioStorage.Playlist.PLAYLIST
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

    public String getPlaylistName() {
        return AudioStorage.Playlist.PLAYLIST;
    }
}
