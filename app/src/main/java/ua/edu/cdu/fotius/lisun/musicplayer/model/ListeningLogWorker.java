package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListeningLogWorker {

    private final String TAG = getClass().getSimpleName();

    private Realm mRealm;
    private Context mContext;

    public ListeningLogWorker(Context context) {
        mRealm = Realm.getInstance(context);
        mContext = context;
    }

    public void close() {
        if(!mRealm.isClosed()) {
            mRealm.close();
        }
    }

    public void synchronizeStorages() {
        new StoragesSyncAsyncTask(mContext).execute();
    }

    //TODO: only for debug. can be deleted
    private void logAll() {
        RealmResults<ListeningLog> logs = mRealm.allObjects(ListeningLog.class);
        for(ListeningLog log : logs) {
            Log.d(TAG, "ID: " + log.getTrackId() + "  COUNT: " + log.getListenedCounter());
        }
    }
}
