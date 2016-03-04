package ua.edu.cdu.fotius.lisun.musicplayer.service;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.model.ListeningLog;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.MathUtils;

public class FavoriteSongDetector {

    private final String TAG = getClass().getSimpleName();

    public static long TRACK_ENDED_POSITION = -1;
    private static long CONSIDER_LISTENED_PERCENTAGE = 50;

    private long mTrackID = -1;
    private long mPlaybackStartedInMillis = -1;
    private long mTrackDuration = -1;

    private Realm mRealm;

    public FavoriteSongDetector(Context context) {
        Log.e(TAG, "Favorite song constructor");
        mRealm = Realm.getInstance(context);
    }

    public void setPlaybackStarted(long trackId, long trackDuration, long startedAtInMillis) {
        Log.e(TAG, "START.AUDIO ID: " + trackId);
        Log.d(TAG, "Start playing at: " + startedAtInMillis);
        mTrackID = trackId;
        mPlaybackStartedInMillis = startedAtInMillis;
        mTrackDuration = trackDuration;
    }

    public void endPlayback(long endedInMillis) {

        if(endedInMillis == TRACK_ENDED_POSITION) {
            endedInMillis = mTrackDuration;
        }

        Log.e(TAG, "END. AUDIO ID: " + mTrackID);
        Log.d(TAG, "end playing at: " + endedInMillis);
        Log.d(TAG, "start at: " + mPlaybackStartedInMillis);
        Log.d(TAG, "played for: " + (endedInMillis - mPlaybackStartedInMillis));

        long listenedPercentagesPart =
                MathUtils.calculatePercentagePart(mTrackDuration,
                        (endedInMillis - mPlaybackStartedInMillis));
        Log.d(TAG, "Listened percentage: " + listenedPercentagesPart);
        if(listenedPercentagesPart >= CONSIDER_LISTENED_PERCENTAGE) {
            ListeningLog logForId  =
                    mRealm.where(ListeningLog.class).equalTo("trackId", mTrackID).findFirst();
            mRealm.beginTransaction();
            if(logForId == null) {
                ListeningLog newLog = mRealm.createObject(ListeningLog.class);
                newLog.setTrackId(mTrackID);
                newLog.setListenedCounter(1);
            } else {
                long counter = logForId.getListenedCounter();
                logForId.setListenedCounter(++counter);
            }
            mRealm.commitTransaction();
        }

        RealmResults<ListeningLog> results = mRealm.allObjects(ListeningLog.class);
        for(ListeningLog l : results) {
            Log.d(TAG, "ID : " + l.getTrackId() + " COUNTER: " + l.getListenedCounter());
        }
    }


}
