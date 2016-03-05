package ua.edu.cdu.fotius.lisun.musicplayer.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListenedUpdaterAsyncTask extends AsyncTask<Long, Void, Void> {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    public ListenedUpdaterAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Long... params) {
        long trackId = params[0];
        Realm mRealm = Realm.getInstance(mContext);

        ListenLog justListenedLog  =
                mRealm.where(ListenLog.class).equalTo(ListenLog.TRACK_ID_ATTR, trackId).findFirst();

        mRealm.beginTransaction();
        if(justListenedLog == null) {
            ListenLog newLog = mRealm.createObject(ListenLog.class);
            newLog.setTrackId(trackId);
            newLog.setListenedCounter(1);
        } else {
            long counter = justListenedLog.getListenedCounter();
            justListenedLog.setListenedCounter(++counter);
        }
        mRealm.commitTransaction();

        //TODO: debug only
        RealmResults<ListenLog> results = mRealm.allObjects(ListenLog.class);
        for(ListenLog l : results) {
            Log.d(TAG, "ID : " + l.getTrackId() + " COUNTER: " + l.getListenedCounter());
        }
        return null;
    }
}
