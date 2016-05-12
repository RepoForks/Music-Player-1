package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class StoragesSyncAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public StoragesSyncAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Set<Long> mediaStoreIds = retrieveMediaStoreIds();

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<ListenLog> modelIds = realm.allObjects(ListenLog.class);
            synchronizeStorages(mediaStoreIds, realm, modelIds);
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
        return null;
    }

    private void synchronizeStorages(Set<Long> mediaStoreIds, Realm realm, RealmResults<ListenLog> modelIds) {
        long id;
        realm.beginTransaction();
        for(int i = modelIds.size() - 1; i >= 0; i--) {
            ListenLog log = modelIds.get(i);
            id = log.getTrackId();
            if(!mediaStoreIds.contains(id)) {
                log.removeFromRealm();
            }
        }
        realm.commitTransaction();
    }

    private Set<Long> retrieveMediaStoreIds() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                AudioStorage.Track.TRACK_ID
        };
        String selection = AudioStorage.Track.IS_MUSIC + " = 1";
        ContentResolver resolver = mContext.getContentResolver();

        Cursor cursor = null;
        Set<Long> set = new HashSet<>();
        try {
            cursor = resolver.query(uri, projection, selection, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                set.add(cursor.getLong(0));
                cursor.moveToNext();
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return set;
    }
}
