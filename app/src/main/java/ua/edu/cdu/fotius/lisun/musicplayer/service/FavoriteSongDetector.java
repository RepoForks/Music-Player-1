package ua.edu.cdu.fotius.lisun.musicplayer.service;

import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.MathUtils;

public class FavoriteSongDetector {

    private final String TAG = getClass().getSimpleName();

    private static long CONSIDER_LISTENED_PERCENTAGE = 50;

    private long mTrackID = -1;
    private long mPlaybackStartedInMillis = -1;
    private long mTrackDuration = -1;

    public void setPlaybackStarted(long trackId, long trackDuration, long startedAtInMillis) {
//        Log.e(TAG, "Start playing: " + trackId + " at: " + startedAtInMillis);
        mTrackID = trackId;
        mPlaybackStartedInMillis = startedAtInMillis;
        mTrackDuration = trackDuration;
    }

    public void endPlayback(long endedInMillis) {
//        Log.e(TAG, "End playing at: " + endedInMillis);
//
//        Log.d(TAG, "start at: " + mPlaybackStartedInMillis);
//        Log.d(TAG, "end at: " + endedInMillis);
//        Log.d(TAG, "played for: " + (endedInMillis - mPlaybackStartedInMillis));

        long listenedPercentagesPart =
                MathUtils.calculatePercentagePart(mTrackDuration,
                        (endedInMillis - mPlaybackStartedInMillis));
//        Log.d(TAG, "Listened percentage: " + listenedPercentagesPart);
        if(listenedPercentagesPart >= CONSIDER_LISTENED_PERCENTAGE) {

        }
    }
}
