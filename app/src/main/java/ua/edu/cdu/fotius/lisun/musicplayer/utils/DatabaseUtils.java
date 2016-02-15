package ua.edu.cdu.fotius.lisun.musicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        cursor.close();

        List valuesList = new ArrayList();
        for (int i = 0; i < trackIds.length; i++) {
            cursor = resolver.query(uri, new String[]{AudioStorage.PlaylistMember.TRACK_ID},
                    AudioStorage.PlaylistMember.TRACK_ID + "=?",
                    new String[]{Long.toString(trackIds[i])}, null);
            /*if this track already exists don't add it*/
            if (cursor.getCount() == 0) {
                ContentValues cv = new ContentValues();
                cv.put(AudioStorage.PlaylistMember.PLAY_ORDER, lastInPlayOrder + i);
                cv.put(AudioStorage.PlaylistMember.TRACK_ID, trackIds[i]);
                valuesList.add(cv);
            }
            cursor.close();
        }
        ContentValues[] contentValues = new ContentValues[valuesList.size()];
        valuesList.toArray(contentValues);
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

    public static long[] queryAlbumsTracks(Context context, long artistID, long[] albumsID) {
        ArrayList<Long> tracksID = new ArrayList<>();
        StringBuffer selectionBuffer = new StringBuffer();
        selectionBuffer.append(AudioStorage.Track.ALBUM_ID + " = ?");
        if (artistID != AudioStorage.WRONG_ID) {
            selectionBuffer.append(" AND " + AudioStorage.Track.ARTIST_ID + " = ?");
        }

        for (int i = 0; i < albumsID.length; i++) {
            String[] selectionArgs = (artistID != AudioStorage.WRONG_ID)
                    ? new String[]{Long.toString(albumsID[i]), Long.toString(artistID)}
                    : new String[]{Long.toString(albumsID[i])};
            tracksID.addAll(queryTracks(context, selectionBuffer.toString(), selectionArgs));
        }
        return toPrimitiveArray(tracksID);
    }

    public static long[] queryArtistsTracks(Context context, long[] artistsID) {
        ArrayList<Long> tracksID = new ArrayList<>();
        String selection = AudioStorage.Track.ARTIST_ID + " = ?";
        for (int i = 0; i < artistsID.length; i++) {
            String[] selectionArgs = new String[]{Long.toString(artistsID[i])};
            tracksID.addAll(queryTracks(context, selection, selectionArgs));
        }
        return toPrimitiveArray(tracksID);
    }

    private static ArrayList<Long> queryTracks(Context context, String selection, String[] selectionArgs) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                AudioStorage.Track.TRACK_ID
        };

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

    public static String makeInBody(long[] ids) {
        if ((ids != null) && (ids.length > 0)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                stringBuffer.append(", " + ids[i]);
            }
            return stringBuffer.toString();
        }
        return ("");
    }

    private static long[] toPrimitiveArray(ArrayList<Long> list) {
        Long[] objectsArray = list.toArray(new Long[list.size()]);
        long[] array = new long[objectsArray.length];
        for (int i = 0; i < objectsArray.length; i++) {
            array[i] = objectsArray[i];
        }
        return array;
    }

    //TODO: only for debug
//    public static void queryParamsInLog(Uri uri, String[] projection, String selection, String[] selectionArgs) {
//        if (uri != null) {
//            Log.d(TAG, "Uri: " + uri.toString());
//        }
//
//        if (projection != null) {
//            Log.d(TAG, "Projection: ");
//            for (int i = 0; i < projection.length; i++) {
//                Log.d(TAG, projection[i]);
//            }
//        }
//
//        if (selection != null) {
//            Log.d(TAG, "Selection: " + selection);
//        }
//
//        if (selectionArgs != null) {
//            Log.d(TAG, "SelectionArgs: ");
//            for (int i = 0; i < selectionArgs.length; i++) {
//                Log.d(TAG, selectionArgs[i]);
//            }
//        }
//    }
}
