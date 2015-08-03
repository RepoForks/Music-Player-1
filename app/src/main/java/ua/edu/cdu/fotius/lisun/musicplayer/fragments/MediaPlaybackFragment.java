package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.OnCallToServiceListener;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.RepeatingImageButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaPlaybackFragment extends Fragment {

    private OnCallToServiceListener mServiceCallbacks;

    private long mStartSeekPos = 0;

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceCallbacks.unbindFromService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_playback, container, false);
        RepeatingImageButton prevButtonCollapseable = (RepeatingImageButton) v.findViewById(R.id.prev_collapseable);
        prevButtonCollapseable.setOnClickListener(mPrevListener);
        //setRepeatListener

        RepeatingImageButton nextButtonCollapseable = (RepeatingImageButton) v.findViewById(R.id.next_collapseable);
        nextButtonCollapseable.setOnClickListener(mNextListener);

        RepeatingImageButton prevButton = (RepeatingImageButton) v.findViewById(R.id.prev);
        prevButton.setOnClickListener(mPrevListener);

        RepeatingImageButton nextButton = (RepeatingImageButton) v.findViewById(R.id.next);
        nextButton.setOnClickListener(mNextListener);

        return v;
    }

    private View.OnClickListener mPrevListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                if (mServiceCallbacks.position() < 2000) {
                    mServiceCallbacks.prev();
                } else {
                    mServiceCallbacks.seek(0);
                    mServiceCallbacks.play();
                }
            } catch (RemoteException e) {
            }
        }
    };

    private View.OnClickListener mNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                mServiceCallbacks.next();
            } catch (RemoteException e) {
            }
        }
    };

    private RepeatingImageButton.RepeatListener mRewListener =
            new RepeatingImageButton.RepeatListener() {
                public void onRepeat(View v, long howlong, int repcnt) {
                    scanBackward(repcnt, howlong);
                }
            };

    private RepeatingImageButton.RepeatListener mFfwdListener =
            new RepeatingImageButton.RepeatListener() {
                public void onRepeat(View v, long howlong, int repcnt) {
                    scanForward(repcnt, howlong);
                }
            };
    /**
     * @param repcnt repeat count
     * @param delta how long was pressed
     */
    private void scanBackward(int repcnt, long delta) {
        try {
            if(repcnt == 0) {
                mStartSeekPos = mServiceCallbacks.position();
                mLastSeekEventTime = 0;
                mSeeking = false;
            } else {
                mSeeking = true;
                if (delta < 5000) {
                    // seek at 10x speed for the first 5 seconds
                    delta = delta * 10;
                } else {
                    // seek at 40x after that
                    delta = 50000 + (delta - 5000) * 40;
                }
                long newpos = mStartSeekPos - delta;
                //if scan backward up to start
                //then go to previous song
                if (newpos < 0) {
                    // move to previous track
                    mService.prev();
                    long duration = mService.duration();
                    mStartSeekPos += duration;
                    newpos += duration;
                }
                if (((delta - mLastSeekEventTime) > 250) || repcnt < 0){
                    mService.seek(newpos);
                    mLastSeekEventTime = delta;
                }
                if (repcnt >= 0) {
                    mPosOverride = newpos;
                } else {
                    mPosOverride = -1;
                }
                refreshNow();
            }
        } catch (RemoteException ex) {
        }
    }

    private void scanForward(int repcnt, long delta) {
        try {
            if(repcnt == 0) {
                mStartSeekPos = mService.position();
                mLastSeekEventTime = 0;
                mSeeking = false;
            } else {
                mSeeking = true;
                if (delta < 5000) {
                    // seek at 10x speed for the first 5 seconds
                    delta = delta * 10;
                } else {
                    // seek at 40x after that
                    delta = 50000 + (delta - 5000) * 40;
                }
                long newpos = mStartSeekPos + delta;
                long duration = mService.duration();
                if (newpos >= duration) {
                    // move to next track
                    mService.next();
                    mStartSeekPos -= duration; // is OK to go negative
                    newpos -= duration;
                }
                if (((delta - mLastSeekEventTime) > 250) || repcnt < 0){
                    mService.seek(newpos);
                    mLastSeekEventTime = delta;
                }
                if (repcnt >= 0) {
                    mPosOverride = newpos;
                } else {
                    mPosOverride = -1;
                }
                refreshNow();
            }
        } catch (RemoteException ex) {
        }
    }
}
