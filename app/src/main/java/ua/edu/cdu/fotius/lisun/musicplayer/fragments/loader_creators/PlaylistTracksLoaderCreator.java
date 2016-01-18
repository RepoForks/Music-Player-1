package ua.edu.cdu.fotius.lisun.musicplayer.fragments.loader_creators;


import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class PlaylistTracksLoaderCreator extends BaseTracksLoaderCreator {

    private long mPlaylistId;

    public PlaylistTracksLoaderCreator(Context context, long playlistId) {
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
                getPrimaryColumn(),
                getTrackIdColumn(),
                getTrackColumn(),
                getArtistColumn(),
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
    public String getTrackIdColumn() {
        return AudioStorage.PlaylistMember.TRACK_ID;
    }

    @Override
    public String getTrackColumn() {
        return AudioStorage.PlaylistMember.TRACK;
    }

    @Override
    public String getArtistColumn() {
        return AudioStorage.PlaylistMember.ARTIST;
    }

    @Override
    public String getAlbumIdColumn() {
        return AudioStorage.PlaylistMember.ALBUM_ID;
    }

    public String getPrimaryColumn() {
        return AudioStorage.PlaylistMember.PRIMARY_ID;
    }

    public String getPlayOrder() {
        return AudioStorage.PlaylistMember.PLAY_ORDER;
    }
}
