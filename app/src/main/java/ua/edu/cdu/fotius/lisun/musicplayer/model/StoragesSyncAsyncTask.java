package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class StoragesSyncAsyncTask extends AsyncTask<Void, Void, Void> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    public StoragesSyncAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // 1. Retrieve all ids from MediaStore
        Set<Long> mediaStoreIds = retrieveMediaStoreIds();
        Log.e(TAG, "MEDIA STORE IDS. SET: ");
        displaySet(mediaStoreIds);
        // 2. Retrieve all ids from Model
        Realm realm = Realm.getInstance(mContext);
        Set<Long> modelIds = retrieveModelIds(realm);
        Log.e(TAG, "MODEL IDS.BEFORE SYNC. SET: ");
        displaySet(modelIds);
        // 3. Delete all Model's ids which doesn't exists in MediaStore
        modelIds = deleteOldIdsFromModel(modelIds, mediaStoreIds);
        Log.e(TAG, "MODEL IDS.AFTER DELETING OLD. SET: ");
        displaySet(modelIds);
        // 4. Add ids which doesn't exist in Model
        addNewIdsToModel(modelIds, mediaStoreIds);
        Log.e(TAG, "MODEL IDS.AFTER ADDING NEW. SET: ");
        displaySet(modelIds);

        //TODO: add to realm

        return null;
    }

    private Set<Long> retrieveMediaStoreIds() {
        // 1. Create cursor
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

        // 2. Iterate through cursor.
        cursor.moveToFirst();
        Set<Long> set = new HashSet<>(cursor.getCount());

        while (!cursor.isAfterLast()) {
            // 3. Add id to set
            set.add(cursor.getLong(0));
            cursor.moveToNext();
        }
        return set;
    }

    private Set<Long> retrieveModelIds(Realm realm) {
        //1. Retrieve all ids from model
        RealmResults<ListeningLog> allLogs = realm.allObjects(ListeningLog.class);
        //2. Add ids to set
        Set<Long> set = new HashSet<>(allLogs.size());
        for (ListeningLog log : allLogs) {
            set.add(log.getTrackId());
        }
        return set;
    }

    private Set<Long> deleteOldIdsFromModel(Set<Long> modelIds, Set<Long> mediaStoreIds) {
        Iterator modelIter = modelIds.iterator();
        Set<Long> result = new HashSet<>();
        long id;
        while (modelIter.hasNext()) {
            id = (long) modelIter.next();
            if(mediaStoreIds.contains(id)) {
               result.add(id);
            }
        }
        return result;
    }

    private void addNewIdsToModel(Set<Long> modelIds, Set<Long> mediaStoreIds) {
        Iterator mediaStoreIter = mediaStoreIds.iterator();
        long id;
        while(mediaStoreIter.hasNext()) {
            id = (long) mediaStoreIter.next();
            if(!modelIds.contains(id)) {
                modelIds.add(id);
            }
        }
    }

    //TODO: only for debug. can be deleted
    private void displaySet(Set<Long> set) {
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Log.d(TAG, "ID: " + iterator.next());
        }
    }
}
