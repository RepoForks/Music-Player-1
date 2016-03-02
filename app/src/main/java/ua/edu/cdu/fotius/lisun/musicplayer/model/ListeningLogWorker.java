package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListeningLogWorker {
    private Context mContext;

    public ListeningLogWorker(Context context) {
        mContext = context;
    }

    public void synchronizeStorages() {
        new StoragesSyncAsyncTask(mContext).execute();
    }

    public void deleteFromLog(long[] ids) {
        new ModelDeletionAsyncTask(mContext)
                .execute(ArrayUtils.toObject(ids));
    }
}
