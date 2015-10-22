package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class DatabaseUtils {

    private static final String TAG = "DatabaseUtils";

    /**
     * @return newly created list id
     */
    public static long createPlaylist(Context context, String name) {
        ContentValues values = new ContentValues(1);
        values.put(AudioStorage.Playlist.PLAYLIST, name);
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
        //TODO: uri == null if this playlist already exists
        return ContentUris.parseId(uri);
    }

    /**
     * @return added tracks quantity
     */
    public static int addToPlaylist(Context context, long playlistId, long[] trackIds) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);

        Cursor cursor = resolver.query(uri, null, null, null, null);
        int lastInPlayOrder = cursor.getColumnCount();

        ContentValues[] contentValues = new ContentValues[trackIds.length];
        for (int i = 0; i < trackIds.length; i++) {
            contentValues[i] = new ContentValues();
            contentValues[i].put(AudioStorage.PlaylistMember.PLAY_ORDER, lastInPlayOrder + i);
            contentValues[i].put(AudioStorage.PlaylistMember.TRACK_ID, trackIds[i]);
        }
        return resolver.bulkInsert(uri, contentValues);
    }
}
