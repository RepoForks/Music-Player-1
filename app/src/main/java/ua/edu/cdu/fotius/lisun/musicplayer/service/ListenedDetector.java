package ua.edu.cdu.fotius.lisun.musicplayer.service;

import android.content.Context;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.model.ListenLogWorker;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.MathUtils;

public class ListenedDetector {

    private final String TAG = getClass().getSimpleName();

    public static long DEFAULT_VALUE = -1;
    private static long CONSIDER_LISTENED_PERCENTAGE = 50;

    private long mTrackID = DEFAULT_VALUE;
    private long mStartedAtInMillis = DEFAULT_VALUE;
    private long mTrackDuration = DEFAULT_VALUE;

    private ListenLogWorker mListenLogWorker;

    public ListenedDetector(Context context) {
        Log.e(TAG, "Favorite song constructor");
        mListenLogWorker = new ListenLogWorker(context);
    }

    public void onPlaybackStarted(long trackId, long trackDuration, long startedAtInMillis) {
        Log.e(TAG, "START.AUDIO ID: " + trackId);
        Log.d(TAG, "Start playing at: " + startedAtInMillis);
        mTrackID = trackId;
        mStartedAtInMillis = startedAtInMillis;
        mTrackDuration = trackDuration;
    }

    public void onPlaybackEnded(long endedAtInMillis) {

        if(endedAtInMillis == DEFAULT_VALUE) {
            endedAtInMillis = mTrackDuration;
        }

        Log.e(TAG, "END. AUDIO ID: " + mTrackID);
        Log.d(TAG, "end playing at: " + endedAtInMillis);
        Log.d(TAG, "start at: " + mStartedAtInMillis);
        Log.d(TAG, "played for: " + (endedAtInMillis - mStartedAtInMillis));

        long listenedPercentage =
                MathUtils.calculatePercentagePart(mTrackDuration,
                        (endedAtInMillis - mStartedAtInMillis));

        Log.d(TAG, "Listened percentage: " + listenedPercentage);

        if(listenedPercentage >= CONSIDER_LISTENED_PERCENTAGE) {
           mListenLogWorker.updateCounter(mTrackID);
        }
    }
}
