package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
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
        Realm realm = Realm.getInstance(mContext);
        RealmResults<ListeningLog> modelIds = realm.allObjects(ListeningLog.class);
        synchronizeStorages(mediaStoreIds, realm, modelIds);
        realm.close();
        return null;
    }

    private void synchronizeStorages(Set<Long> mediaStoreIds, Realm realm, RealmResults<ListeningLog> modelIds) {
        long id;

        for(int i = modelIds.size() - 1; i >= 0; i--) {
            ListeningLog log = modelIds.get(i);
            id = log.getTrackId();
            if(mediaStoreIds.contains(id)) {
                //just preparing this set
                mediaStoreIds.remove(id);
            } else { /*does not contain, so delete from Realm*/
                realm.beginTransaction();
                log.removeFromRealm();
                realm.commitTransaction();
            }
        }

        Iterator<Long> iter = mediaStoreIds.iterator();
        while (iter.hasNext()) {
            id = iter.next();
            realm.beginTransaction();
            ListeningLog newObject = realm.createObject(ListeningLog.class);
            newObject.setTrackId(id);
            newObject.setListenedCounter(0);
            realm.commitTransaction();
        }
    }

    private Set<Long> retrieveMediaStoreIds() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                AudioStorage.Track.TRACK_ID
        };
        String selection = AudioStorage.Track.IS_MUSIC + " = 1";
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(uri, projection, selection, null, null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();
        Set<Long> set = new HashSet<>(cursor.getCount());

        while (!cursor.isAfterLast()) {
            set.add(cursor.getLong(0));
            cursor.moveToNext();
        }
        return set;
    }

    //TODO: only for debug. can be deleted
    public static Set<Long> retrieveModelIds(Realm realm) {
        //1. Retrieve all ids from model
        RealmResults<ListeningLog> allLogs = realm.allObjects(ListeningLog.class);
        //2. Add ids to set
        Set<Long> set = new HashSet<>(allLogs.size());
        for (ListeningLog log : allLogs) {
            set.add(log.getTrackId());
        }
        return set;
    }

    //TODO: only for debug. can be deleted
    public static void displaySet(Set<Long> set) {
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Log.d("DISPLAY SET", "ID: " + iterator.next());
        }
    }
}
