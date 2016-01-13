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

    //TODO: do i need all this stuff
    @Override
    public String[] getProjection() {
        return new String[]{
                getPrimaryColumnName(),
                getTrackIdColumnName(),
                getTrackColumnName(),
                getArtistColumnName(),
                getPlayOrder()
        };
    }

    @Override
    public String getSelection() {
        return null;
        //return AudioStorage.PlaylistMember.PLAYLIST_ID + "=?";
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
        //return new String[] {Long.toString(mPlaylistId)};
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

    public String getPrimaryColumnName() {
        return AudioStorage.PlaylistMember.PRIMARY_ID;
    }

    public String getPlayOrder() {
        return AudioStorage.PlaylistMember.PLAY_ORDER;
    }
}
