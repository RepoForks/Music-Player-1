package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class ListenedUpdaterAsyncTask extends AsyncTask<Long, Void, Void> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    public ListenedUpdaterAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Long... params) {
        long trackId = params[0];
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            ListenLog justListenedLog =
                    realm.where(ListenLog.class).equalTo(ListenLog.TRACK_ID_ATTR, trackId).findFirst();
            if (justListenedLog == null) {
                ContentResolver resolver = mContext.getContentResolver();
                String genre = retrieveGenre(resolver, trackId);
                String artist = retrieveArtist(resolver, trackId);
                String album = retrieveAlbum(resolver, trackId);
                realm.beginTransaction();
                ListenLog newLog = realm.createObject(ListenLog.class);
                newLog.setTrackId(trackId);
                newLog.setListenedCounter(1);
                newLog.setGenre(genre);
                newLog.setArtist(artist);
                newLog.setAlbum(album);
                realm.commitTransaction();
            } else {
                realm.beginTransaction();
                long counter = justListenedLog.getListenedCounter();
                justListenedLog.setListenedCounter(++counter);
                realm.commitTransaction();
            }
            //TODO: debug only
            RealmResults<ListenLog> results = realm.allObjects(ListenLog.class);
            for (ListenLog l : results) {
                Log.d(TAG, "ID : " + l.getTrackId() + " COUNTER: " + l.getListenedCounter() +
                        " GENRE: " + l.getGenre() + " ARTIST: " + l.getArtist() + " ALBUM: " + l.getAlbum());
            }

        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return null;
    }

    private String retrieveGenre(ContentResolver contentResolver, long trackId) {
        Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int) trackId);
        String[] projection = new String[]{
                AudioStorage.Genres.GENRE
        };
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        String genre = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                genre = cursor.getString(0);
            }
            cursor.close();
        }
        return genre;
    }

    private String retrieveArtist(ContentResolver contentResolver, long trackId) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                AudioStorage.Track.ARTIST
        };
        String selection = AudioStorage.Track.TRACK_ID + "=?";
        String[] selectionArgs = new String[]{
                Long.toString(trackId)
        };
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);
        String artist = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                artist = cursor.getString(0);
            }
            cursor.close();
        }
        return artist;
    }

    private String retrieveAlbum(ContentResolver contentResolver, long trackId) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                AudioStorage.Track.ALBUM
        };
        String selection = AudioStorage.Track.TRACK_ID + "=?";
        String[] selectionArgs = new String[]{
                Long.toString(trackId)
        };
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);
        String album = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                album = cursor.getString(0);
            }
            cursor.close();
        }
        return album;
    }
}
