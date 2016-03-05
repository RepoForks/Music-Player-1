package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListenLogWorker {
    private Context mContext;

    public ListenLogWorker(Context context) {
        mContext = context;
    }

    public void updateCounter(long trackId) {
        new ListenedUpdaterAsyncTask(mContext).execute(trackId);
    }

    public void synchronizeStorages() {
        new StoragesSyncAsyncTask(mContext).execute();
    }

    public void deleteFromLog(long[] ids) {
        new ModelDeletionAsyncTask(mContext)
                .execute(ArrayUtils.toObject(ids));
    }
}
