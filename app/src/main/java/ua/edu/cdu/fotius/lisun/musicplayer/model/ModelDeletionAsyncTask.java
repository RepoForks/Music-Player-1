package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.Context;
import android.os.AsyncTask;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ModelDeletionAsyncTask extends AsyncTask<Long, Void, Void> {
    private Context mContext;

    public ModelDeletionAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Long... ids) {
        Realm realm = null;
        try {
            realm = Realm.getInstance(mContext);
            RealmResults<ListenLog> allLogs = realm.allObjects(ListenLog.class);
            RealmQuery query = allLogs.where();
            for (int i = 0; i < ids.length; i++) {
                query.equalTo(ListenLog.TRACK_ID_ATTR, ids[i]);
                if (i != (ids.length - 1)) {
                    query.or();
                }
            }

            RealmResults<ListenLog> forDeletion = query.findAll();

            realm.beginTransaction();
            for (int i = forDeletion.size() - 1; i >= 0; i--) {
                forDeletion.get(i).removeFromRealm();
            }
            realm.commitTransaction();
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
        return null;
    }
}
