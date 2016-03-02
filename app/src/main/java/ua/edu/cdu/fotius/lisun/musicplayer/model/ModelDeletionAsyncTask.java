package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ModelDeletionAsyncTask extends AsyncTask<Long, Void, Void> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    public ModelDeletionAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Long... ids) {
        Realm realm = Realm.getInstance(mContext);

        //TODO: only debug
        Log.e(TAG, "FOR DELETION");
        for(int i = 0; i < ids.length; i++) {
            Log.d(TAG, "ID : " + ids[i]);
        }

        //TODO: only debug
        Log.e(TAG, "MODEL BEFOR DELETION");
        Set<Long> set = StoragesSyncAsyncTask.retrieveModelIds(realm);
        StoragesSyncAsyncTask.displaySet(set);


        RealmResults<ListeningLog> allLogs = realm.allObjects(ListeningLog.class);
        RealmQuery query = allLogs.where();
        for(int i = 0; i < ids.length; i++) {
            query.equalTo("trackId", ids[i]);
            if(i != (ids.length - 1)) {
                query.or();
            }
        }

        RealmResults<ListeningLog> forDeletion =  query.findAll();

        realm.beginTransaction();
        for(int i = forDeletion.size() - 1; i >= 0; i--) {
            forDeletion.get(i).removeFromRealm();
        }
        realm.commitTransaction();

        //TODO: only debug
        Log.e(TAG, "MODEL AFTER DELETION");
        set = StoragesSyncAsyncTask.retrieveModelIds(realm);
        StoragesSyncAsyncTask.displaySet(set);

        realm.close();
        return null;
    }
}
