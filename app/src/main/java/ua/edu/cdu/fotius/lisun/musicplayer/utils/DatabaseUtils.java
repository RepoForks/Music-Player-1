package ua.edu.cdu.fotius.lisun.musicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.cursorloader_creators.AlbumTracksCursorLoaderCreator;

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

    public static String queryAlbumArtPath(Context context, long albumID) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{AudioStorage.Album.ALBUM_ART};
        String selection = AudioStorage.Album.ALBUM_ID + "=?";
        String[] selectionArgs = new String[]{Long.toString(albumID)};

        Cursor c = contentResolver.query(uri, projection, selection, selectionArgs, null);

        String path = null;
        if (c != null) {
            if (c.moveToFirst()) {
                path = c.getString(c.getColumnIndexOrThrow(AudioStorage.Album.ALBUM_ART));
            }
            c.close();
        }
        return path;
    }

    public static long[] queryAlbumsTracks(Context context, long[] albumsID) {
       return queryAllTracks(context, AudioStorage.Track.ALBUM_ID, albumsID);
    }

    public static long[] queryArtistsTracks(Context context, long[] artistsID) {
        return queryAllTracks(context, AudioStorage.Track.ARTIST_ID, artistsID);
    }

    private static long[] queryAllTracks(Context context, String selectionColumn, long[] selectionArgs) {
        ArrayList<Long> tracksID = new ArrayList<>();
        for(int i = 0; i < selectionArgs.length; i++) {
            tracksID.addAll(queryTracks(context, selectionColumn, selectionArgs[i]));
        }
        Long[] trackIDsArray = tracksID.toArray(new Long[tracksID.size()]);
        return toPrimitive(trackIDsArray);
    }

    private static ArrayList<Long> queryTracks(Context context, String selectionColumn, long selectionArg) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {
                AudioStorage.Track.TRACK_ID
        };

        String selection = selectionColumn + " = ?";
        String[] selectionArgs = new String[]{Long.toString(selectionArg)};

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);

        ArrayList<Long> trackIds = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIdx = 0;
                while (!cursor.isAfterLast()) {
                    columnIdx = cursor.getColumnIndexOrThrow(AudioStorage.Track.TRACK_ID);
                    trackIds.add(cursor.getLong(columnIdx));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return trackIds;
    }

    private static long[] toPrimitive(Long[] objects) {
        long[] array = new long[objects.length];
        for(int i = 0; i < objects.length; i++) {
            array[i] = objects[i];
        }
        return array;
    }

    //TODO: only for debug
    public static void queryParamsInLog(Uri uri, String[] projection, String selection, String[] selectionArgs) {
        if (uri != null) {
            Log.d(TAG, "Uri: " + uri.toString());
        }

        if (projection != null) {
            Log.d(TAG, "Projection: ");
            for (int i = 0; i < projection.length; i++) {
                Log.d(TAG, projection[i]);
            }
        }

        if (selection != null) {
            Log.d(TAG, "Selection: " + selection);
        }

        if (selectionArgs != null) {
            Log.d(TAG, "SelectionArgs: ");
            for (int i = 0; i < selectionArgs.length; i++) {
                Log.d(TAG, selectionArgs[i]);
            }
        }
    }
}
