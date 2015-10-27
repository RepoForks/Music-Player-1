package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class PlaylistTracksCursorLoaderFactory extends AbstractCursorLoaderFactory {

    private long mPlaylistId;

    public PlaylistTracksCursorLoaderFactory(Context context, long playlistId) {
        super(context);
        mPlaylistId = playlistId;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistId);
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                AudioStorage.PlaylistMember.TRACK_ID_WITHIN_PLAYLIST,
                AudioStorage.PlaylistMember.TRACK_ID,
                AudioStorage.PlaylistMember.TRACK,
                AudioStorage.PlaylistMember.ARTIST
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
        return AudioStorage.PlaylistMember.SORT_ORDER;
    }

    @Override
    public String getTrackIdColumnName() {
        return AudioStorage.PlaylistMember.TRACK_ID;
    }

    @Override
    public String getTrackColumnName() {
        return AudioStorage.PlaylistMember.TRACK;
    }

    @Override
    public String getArtistColumnName() {
        return AudioStorage.PlaylistMember.ARTIST;
    }
}
