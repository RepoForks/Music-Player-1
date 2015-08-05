package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.OnCallToServiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.RepeatingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaPlaybackFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private final int SEEK_BAR_MAX = 1000;
    private final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    private OnCallToServiceListener mServiceCallbacks;

    private long mStartSeekPos = 0;

    private View mMainLayout;
    private TextView mTrackName;
    private TextView mArtistName;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;

    public MediaPlaybackFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mServiceCallbacks = (OnCallToServiceListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mServiceCallbacks.bindToService();

        mMainLayout = getActivity().getLayoutInflater().inflate(R.layout.fragment_media_playback, null, false);
        initializeControlButtons();
        initializeTrackInfoViews();
    }

    private void initializeControlButtons() {
        View layout = mMainLayout;

        RepeatingImageButton prevButtonCollapseable =
                (RepeatingImageButton) layout.findViewById(R.id.prev_collapseable);
        prevButtonCollapseable.setOnClickListener(mPrevListener);
        //prevButtonCollapseable.setRepeatListener(mRewindListener);

        RepeatingImageButton nextButtonCollapseable =
                (RepeatingImageButton) layout.findViewById(R.id.next_collapseable);
        nextButtonCollapseable.setOnClickListener(mNextListener);
        //nextButtonCollapseable.setRepeatListener(mForwardListener);

        RepeatingImageButton prevButton =
                (RepeatingImageButton) layout.findViewById(R.id.prev);
        prevButton.setOnClickListener(mPrevListener);
        //prevButton.setRepeatListener(mRewindListener);

        RepeatingImageButton nextButton =
                (RepeatingImageButton) layout.findViewById(R.id.next);
        nextButton.setOnClickListener(mNextListener);
        //nextButton.setRepeatListener(mForwardListener);
    }

    private void initializeTrackInfoViews() {
        View layout = mMainLayout;

        mTrackName = (TextView) layout.findViewById(R.id.track_title);
        mArtistName = (TextView) layout.findViewById(R.id.artist_name);

        mSeekBar = (SeekBar) layout.findViewById(R.id.seek_bar);
        mSeekBar.setMax(SEEK_BAR_MAX);

        mCurrentTime = (TextView) layout.findViewById(R.id.current_time);
        mTotalTime = (TextView) layout.findViewById(R.id.total_time);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        getActivity().registerReceiver(mStatusListener, actionFilter);
        queueNextRefresh(1);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mStatusListener);
        mHandler.removeMessages(REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceCallbacks.unbindFromService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mMainLayout;
    }

    private View.OnClickListener mPrevListener = new View.OnClickListener() {
        public void onClick(View v) {

            long changeTrackThresholdInMillis = 2000;
            if (mServiceCallbacks.getPlayingPosition() < changeTrackThresholdInMillis) {
                mServiceCallbacks.prev();
            } else {
                mServiceCallbacks.seek(0);
                mServiceCallbacks.play();
            }

        }
    };

    private View.OnClickListener mNextListener = new View.OnClickListener() {
        public void onClick(View v) {

            mServiceCallbacks.next();

        }
    };

//    private RepeatingImageButton.RepeatListener mRewindListener =
//            new RepeatingImageButton.RepeatListener() {
//                public void onRepeat(View v, long howLongWasPressed, int repeatCount) {
//                    scanBackward(repeatCount, howLongWasPressed);
//                }
//            };
//
//    private RepeatingImageButton.RepeatListener mForwardListener =
//            new RepeatingImageButton.RepeatListener() {
//                public void onRepeat(View v, long howLongWasPressed, int repeatCount) {
//                    scanForward(repeatCount, howLongWasPressed);
//                }
//            };
//
//    //TODO:
//    private void scanBackward(int repeatCount, long howLongWasPressed) {
//
//        try {
//            if(repeatCount == 0) {
//                mStartSeekPos = mServiceCallbacks.getPlayingPosition();
//                //mLastSeekEventTime = 0;
//            } else {
//                long seekLeapTime = getSeekLeapTime(howLongWasPressed);
//                long newPosition = mStartSeekPos - seekLeapTime;
//                //if scan backward up to start
//                //then go to previous song
//                if (newPosition < 0) {
//                    // move to previous track
//                    mServiceCallbacks.prev();
//                    long duration = mServiceCallbacks.getTrackDuration();
//                    mStartSeekPos += duration;
//                    newPosition += duration;
//                }
//                mServiceCallbacks.seek(newPosition);
////                if (repeatCount >= 0) {
////                    mPosOverride = newpos;
////                } else {
////                    mPosOverride = -1;
////                }
//                //refreshNow();
//            }
//        } catch (RemoteException ex) {
//        }
//    }
//
//    private long getSeekLeapTime(long howLongWasPressed) {
//        if (howLongWasPressed < 5000) {
//            // seek at 10x speed for the first 5 seconds
//            return (howLongWasPressed * 10);
//        } else {
//            // seek at 40x after that
//            //return (50000 + (howLongWasPressed - 5000) * 40);
//            return ((howLongWasPressed - 5000) * 40);
//        }
//    }
//
//    //TODO:
//    private void scanForward(int repeatCount, long howLongWasPressed) {
//
//        try {
//            if(repeatCount == 0) {
//                mStartSeekPos = mServiceCallbacks.getPlayingPosition();
//                //mLastSeekEventTime = 0;
//            } else {
//                long seekLeapTime = getSeekLeapTime(howLongWasPressed);
//                long newPosition = mStartSeekPos + seekLeapTime;
//                long duration = mServiceCallbacks.getTrackDuration();
//                if (newPosition >= duration) {
//                    // move to next track
//                    mServiceCallbacks.next();
//                    mStartSeekPos -= duration; // is OK to go negative
//                    newPosition -= duration;
//                }
//
//                mServiceCallbacks.seek(newPosition);
//
////                if (repeatCount >= 0) {
////                    mPosOverride = newpos;
////                } else {
////                    mPosOverride = -1;
////                }
////                refreshNow();
//            }
//        } catch (RemoteException ex) {
//        }
//    }

    private void refreshTrackInfoAndTotalTime() {
        if (!mServiceCallbacks.isBoundToService()) {
            return;
        }
        String trackName = mServiceCallbacks.getTrackName();
        String artistName = mServiceCallbacks.getArtistName();
        long duration = mServiceCallbacks.getTrackDuration();

        if ((trackName != null) && (artistName != null) && (duration > 0)) {
            mTrackName.setText(trackName);
            mArtistName.setText(artistName);
            mTotalTime.setText(TimeUtils.makeTimeString(getActivity(),
                    duration / SEEK_BAR_MAX));
        } else {
            handleRefreshingError();
        }
    }

    private void handleRefreshingError() {
        mTrackName.setText("");
        mArtistName.setText("");
        mTotalTime.setText("--:--");
        mSeekBar.setProgress(0);
        mCurrentTime.setText("--:--");
        //TODO: would be great to go to next song in this case
    }

    private long refreshSeekBarAndCurrentTime() {
        if (!mServiceCallbacks.isBoundToService()) {
            return DEFAULT_REFRESH_DELAY_IN_MILLIS;
        }
        long position = mServiceCallbacks.getPlayingPosition();
        long duration = mServiceCallbacks.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (SEEK_BAR_MAX * position / duration);
            mSeekBar.setProgress(progress);
            mSeekBar.refreshDrawableState();
            mCurrentTime.setText(TimeUtils.makeTimeString(getActivity(),
                    position / SEEK_BAR_MAX));
            return getSmoothRefreshTime(position, duration);
        } else {
            handleRefreshingError();
        }
        return DEFAULT_REFRESH_DELAY_IN_MILLIS;
    }

    private long getSmoothRefreshTime(final long position, final long duration) {
        // calculate the number of milliseconds until the next full second, so
        // the counter can be updated at just the right time
        long remaining = TimeUtils.MILLIS_IN_SECOND - (position % TimeUtils.MILLIS_IN_SECOND);
        // approximate how often we would need to refresh the slider to
        // move it smoothly
        int width = mSeekBar.getWidth();
        if (width == 0) width = 320;
        long smoothRefreshTime = duration / width;
        if (smoothRefreshTime > remaining) return remaining;
        if (smoothRefreshTime < 20) return 20;
        return smoothRefreshTime;
    }


    private void queueNextRefresh(long refreshDelay) {
        Message handlerMessage = mHandler.obtainMessage(REFRESH);
        mHandler.removeMessages(REFRESH);
        mHandler.sendMessageDelayed(handlerMessage, refreshDelay);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    long nextRefreshDelay = refreshSeekBarAndCurrentTime();
                    queueNextRefresh(nextRefreshDelay);
                    break;
            }
        }
    };

    private BroadcastReceiver mStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MediaPlaybackService.META_CHANGED)) {
                refreshTrackInfoAndTotalTime();
                queueNextRefresh(1);
            } else if (action.equals(MediaPlaybackService.PLAYSTATE_CHANGED)) {
//                setPauseButtonImage();
            }
        }
    };
}
