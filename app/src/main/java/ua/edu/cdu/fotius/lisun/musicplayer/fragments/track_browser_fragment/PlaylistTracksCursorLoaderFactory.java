package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;


import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class PlaylistTracksCursorLoaderFactory extends TracksCursorLoaderFactory {

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
                AudioStorage.PlaylistMember.ID_WITHIN_PLAYLIST,
                AudioStorage.PlaylistMember.TRACK_ID,
                AudioStorage.PlaylistMember.TRACK,
                AudioStorage.PlaylistMember.ARTIST,
                AudioStorage.PlaylistMember.PLAY_ORDER
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

    @Override
    public String getAlbumIdColumnName() {
        return AudioStorage.PlaylistMember.ALBUM_ID;
    }

    public String getId() {
        return AudioStorage.PlaylistMember.ID_WITHIN_PLAYLIST;
    }

    public String getPlayOrder() {
        return AudioStorage.PlaylistMember.PLAY_ORDER;
    }
}
