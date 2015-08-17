package ua.edu.cdu.fotius.lisun.musicplayer.fragments.track_browser_fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.TrackBrowserFragment;

public class TrackCursorLoaderFactory {

    private long mAlbumId = TrackBrowserFragment.ID_NOT_SET;
    private long mPlaylistId = TrackBrowserFragment.ID_NOT_SET;

    private Context mContext;

    public TrackCursorLoaderFactory(Context context) {
        mContext = context;
    }

    public void setAlbumId(long albumId) {
        if (albumId == TrackBrowserFragment.ID_NOT_SET)
            return;
        mAlbumId = albumId;
        mPlaylistId = TrackBrowserFragment.ID_NOT_SET;
    }

    public void setPlaylistId(long playlistId) {
        if (playlistId == TrackBrowserFragment.ID_NOT_SET)
            return;
        mPlaylistId = playlistId;
        mAlbumId = TrackBrowserFragment.ID_NOT_SET;
    }

//        return new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//        projection,
//                (mAlbumId == ID_NOT_SET) ? null : MediaStore.Audio.Media.ALBUM_ID + " = ?",
//                (mAlbumId == ID_NOT_SET) ? null : new String[]{Long.toString(mAlbumId)},
//        AudioStorage.TrackBrowser.SORT_ORDER);

    private final String TAG = getClass().getSimpleName();

    public CursorLoader build() {
        Log.d(TAG, "albumId " + mAlbumId + "   playlistId" + mPlaylistId);
        Log.d(TAG, "BUILD. Uri: " + getUri());

        String[] proj = getProjection();
        Log.d(TAG, "Progetction: " + proj);
        if (proj != null) {
            for (int i = 0; i < proj.length; i++) {
                Log.d(TAG, "P" + i + ": " + proj[i]);
            }
        }
        Log.d(TAG, "Selection: " + getSelection());

        String[] selArgs = getSelectionArgs();
        Log.d(TAG, "SelectionArgs: " + selArgs);
        if (selArgs != null) {
            for (int i = 0; i < selArgs.length; i++) {
                Log.d(TAG, "S" + i + ": " + selArgs[i]);
            }
        }

//        ( + " | Projection:" + getProjection() +
//                " | Selection: " + getSelection() + " | SelectionArgs: " + getSelectionArgs() + " | SortOrder: " + getSortOrder());

        return new CursorLoader(mContext, getUri(), getProjection(),
                getSelection(), getSelectionArgs(), getSortOrder());
    }

    private Uri getUri() {
        if (isPlaylistIdDefined()) {
            return MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistId);
        }
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    private String[] getProjection() {
        if (isPlaylistIdDefined()) {
            return getPlaylistProjection();
        }
        return getDefaultProjection();
    }

    private String[] getPlaylistProjection() {
        return new String[]{
                AudioStorage.PlaylistMembers.TRACK_ID_WITHIN_PLAYLIST,
                AudioStorage.PlaylistMembers.TRACK_ID,
                AudioStorage.PlaylistMembers.TRACK,
                AudioStorage.PlaylistMembers.ARTIST
        };
    }

    private String[] getDefaultProjection() {
        return new String[]{
                AudioStorage.TrackBrowser.TRACK_ID,
                AudioStorage.TrackBrowser.TRACK,
                AudioStorage.TrackBrowser.ARTIST
        };
    }

    private boolean isAlbumIdDefined() {
        return (mAlbumId != TrackBrowserFragment.ID_NOT_SET) &&
                (mPlaylistId == TrackBrowserFragment.ID_NOT_SET);
    }

    private boolean isPlaylistIdDefined() {
        return (mPlaylistId != TrackBrowserFragment.ID_NOT_SET)
                && (mAlbumId == TrackBrowserFragment.ID_NOT_SET);
    }

    private String getSelection() {
        if (isAlbumIdDefined()) {
            return AudioStorage.TrackBrowser.ALBUM_ID + " = ?";
        }
        /*Default. Will query all tracks.*/
        return null;
    }

    private String[] getSelectionArgs() {
        if (isAlbumIdDefined()) {
            return new String[]{Long.toString(mAlbumId)};
        }
        /*Default. Will query all tracks.*/
        return null;
    }

    private String getSortOrder() {
        if (isPlaylistIdDefined()) {
            return AudioStorage.PlaylistMembers.SORT_ORDER;
        }
        return AudioStorage.TrackBrowser.SORT_ORDER;
    }

    public String getTrackIdColumnName() {
        if(isPlaylistIdDefined()) {
            return AudioStorage.PlaylistMembers.TRACK_ID;
        }
        return AudioStorage.TrackBrowser.TRACK_ID;
    }

    public String getTrackColumnName() {
        if (isPlaylistIdDefined()) {
            return AudioStorage.PlaylistMembers.TRACK;
        }
        return AudioStorage.TrackBrowser.TRACK;
    }

    public String getArtistColumnName() {
        if (isPlaylistIdDefined()) {
            return AudioStorage.PlaylistMembers.ARTIST;
        }
        return AudioStorage.TrackBrowser.ARTIST;
    }

//        private String[] getPlaylistMembers(long playlistId) {
//            //TODO: Make query to MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistId))
//            ContentResolver contentResolver = mContext.getContentResolver();
//            //query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
//            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistId);
//            String[] projection = new String[] {
//                    AudioStorage.UserPlaylistsBrowser.
//            }
//            contentResolver.query(uri, )
//            return null;
//        }

    //private class PlaylistMembersQuery
}
