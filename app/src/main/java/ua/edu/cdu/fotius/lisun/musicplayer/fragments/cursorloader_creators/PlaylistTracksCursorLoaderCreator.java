package ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators;


import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class PlaylistTracksCursorLoaderCreator extends AbstractTracksCursorLoaderCreator {

    private long mPlaylistId;

    public PlaylistTracksCursorLoaderCreator(Context context, long playlistId) {
        super(context);
        mPlaylistId = playlistId;
    }

    @Override
    public int getLoaderId() {
        return PLAYLIST_TRACKS_LOADER_ID;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistId);
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                getIdWithinPlaylistColumnName(),
                getTrackIdColumnName(),
                getTrackColumnName(),
                getArtistColumnName(),
                getPlayOrder()
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

    public String getIdWithinPlaylistColumnName() {
        return AudioStorage.PlaylistMember.ID_WITHIN_PLAYLIST;
    }

    public String getPlayOrder() {
        return AudioStorage.PlaylistMember.PLAY_ORDER;
    }
}
