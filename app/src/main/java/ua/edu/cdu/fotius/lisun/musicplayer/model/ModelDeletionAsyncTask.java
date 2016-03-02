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
    protected Void doInBackground(Long... params) {
        Set<Long> deletionSet = arrayToSet(params);
        Realm realm = Realm.getInstance(mContext);
        RealmResults<ListeningLog> allLogs = realm.allObjects(ListeningLog.class);
        for(int i = allLogs.size() - 1; i >= 0; i--) {
            ListeningLog log = allLogs.get(i);
            if(deletionSet.contains(log.getTrackId())) {
                realm.beginTransaction();
                log.removeFromRealm();
                realm.commitTransaction();
            }
        }
        realm.close();
        return null;
    }

    private Set<Long> arrayToSet(Long[] ids) {
        return new HashSet<Long>(Arrays.asList(ids));
    }
}
