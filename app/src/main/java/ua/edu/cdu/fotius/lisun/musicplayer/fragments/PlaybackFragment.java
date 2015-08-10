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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceInterface;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.RepeatingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaybackFragment extends Fragment implements ServiceConnectionObserver {

    private final String TAG = getClass().getSimpleName();

    private final int SEEK_BAR_MAX = 1000;
    private final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    private ServiceInterface mFragmentServiceMediator;

    private long mStartSeekPos = 0;

    private TextView mTrackName;
    private TextView mArtistName;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private ImageButton mPlayPauseButton;
    private ImageButton mPlayPauseAdditionalButton;
    //TODO: collapseable --> minimal
    private ImageButton mRepeatButton;

    public PlaybackFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentServiceMediator = (ServiceInterface) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mFragmentServiceMediator.bindToService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_playback, container, false);
        initializeAdditionalControlPanel(v);
        initializeMainControlPanel(v);
        initializeTrackInfoViews(v);
        return v;
    }

    private void initializeAdditionalControlPanel(View layout) {
        RepeatingImageButton prevAdditionalButton =
                (RepeatingImageButton) layout.findViewById(R.id.prev_additional);
        prevAdditionalButton.setOnClickListener(mPrevListener);
        prevAdditionalButton.setRepeatListener(mRewindListener);

        RepeatingImageButton nextAdditionalButton =
                (RepeatingImageButton) layout.findViewById(R.id.next_additional);
        nextAdditionalButton.setOnClickListener(mNextListener);
        nextAdditionalButton.setRepeatListener(mForwardListener);

        mPlayPauseAdditionalButton = (ImageButton) layout.findViewById(R.id.pause_additional);
        mPlayPauseAdditionalButton.setOnClickListener(mPlayPauseListener);
    }

    private void initializeMainControlPanel(View layout) {
        RepeatingImageButton prevButton =
                (RepeatingImageButton) layout.findViewById(R.id.prev);
        prevButton.setOnClickListener(mPrevListener);
        prevButton.setRepeatListener(mRewindListener);

        RepeatingImageButton nextButton =
                (RepeatingImageButton) layout.findViewById(R.id.next);
        nextButton.setOnClickListener(mNextListener);
        nextButton.setRepeatListener(mForwardListener);

        mPlayPauseButton = (ImageButton) layout.findViewById(R.id.pause);
        mPlayPauseButton.setOnClickListener(mPlayPauseListener);

        mRepeatButton = (ImageButton) layout.findViewById(R.id.repeat);
        mRepeatButton.setOnClickListener(mRepeatListener);
    }

    private void initializeTrackInfoViews(View layout) {
        mTrackName = (TextView) layout.findViewById(R.id.track_title);
        mArtistName = (TextView) layout.findViewById(R.id.artist_name);

        mSeekBar = (SeekBar) layout.findViewById(R.id.seek_bar);
        mSeekBar.setMax(SEEK_BAR_MAX);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mCurrentTime = (TextView) layout.findViewById(R.id.current_time);
        mTotalTime = (TextView) layout.findViewById(R.id.total_time);
    }

    private View.OnClickListener mPrevListener = new View.OnClickListener() {
        public void onClick(View v) {
            long changeTrackThresholdInMillis = 2000;
            if (mFragmentServiceMediator.getPlayingPosition() < changeTrackThresholdInMillis) {
                mFragmentServiceMediator.prev();
            } else {
                mFragmentServiceMediator.seek(0);
                mFragmentServiceMediator.play();
            }
        }
    };

    private RepeatingImageButton.RepeatListener mRewindListener =
            new RepeatingImageButton.RepeatListener() {
                public void onRepeat(View v, long howLongWasPressed, int repeatCount) {
                    scanBackward(repeatCount, howLongWasPressed);
                }
            };

    private void scanBackward(int repeatCount, long howLongWasPressed) {
        if (repeatCount == 0) {
            mStartSeekPos = mFragmentServiceMediator.getPlayingPosition();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = mStartSeekPos - seekLeapTime;
            if (newPosition <= 0) {
                // move to previous track
                mFragmentServiceMediator.prev();
                newPosition = 0;
            }
            mFragmentServiceMediator.seek(newPosition);
            refreshSeekBarAndCurrentTime();
        }
    }

    private long getSeekLeapDelta(long howLongWasPressed) {
        if (howLongWasPressed < 5000) {
            // seek at 10x speed for the first 5 seconds
            return (howLongWasPressed * 10);
        } else {
            // seek at 40x after that
            return (50000 /*5000 millis in 10x speed*/
                    + (howLongWasPressed - 5000) * 40);
        }
    }

    private View.OnClickListener mNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            mFragmentServiceMediator.next();
        }
    };

    private RepeatingImageButton.RepeatListener mForwardListener =
            new RepeatingImageButton.RepeatListener() {
                public void onRepeat(View v, long howLongWasPressed, int repeatCount) {
                    scanForward(repeatCount, howLongWasPressed);
                }
            };

    private void scanForward(int repeatCount, long howLongWasPressed) {
        if (repeatCount == 0) {
            mStartSeekPos = mFragmentServiceMediator.getPlayingPosition();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = mStartSeekPos + seekLeapTime;
            long duration = mFragmentServiceMediator.getTrackDuration();
            if (newPosition >= duration) {
                // move to next track
                mFragmentServiceMediator.next();
                newPosition -= duration;
                mStartSeekPos -= duration; // is OK to go negative
            }
            mFragmentServiceMediator.seek(newPosition);
            refreshSeekBarAndCurrentTime();
        }
    }

    private View.OnClickListener mPlayPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playPause();
            setPlayPaseButtonsImage();
        }
    };

    private void playPause() {
        if(mFragmentServiceMediator.isPlaying()) {
            mFragmentServiceMediator.pause();
        } else {
            mFragmentServiceMediator.play();
        }
    }

    private void setPlayPaseButtonsImage() {
        if(mFragmentServiceMediator.isPlaying()) {
            mPlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            View mAdditionalControlPanel = (View) mPlayPauseAdditionalButton.getParent();
            //if(mAdditionalControlPanel.getVisibility() != View.GONE) {
                mPlayPauseAdditionalButton.setImageResource(android.R.drawable.ic_media_pause);
            //}
        } else {
            mPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
            View mAdditionalControlPanel = (View) mPlayPauseAdditionalButton.getParent();
            //if(mAdditionalControlPanel.getVisibility() != View.GONE) {
                mPlayPauseAdditionalButton.setImageResource(android.R.drawable.ic_media_play);
            //}
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        getActivity().registerReceiver(mStatusListener, actionFilter);
        long nextRefreshDelay = refreshSeekBarAndCurrentTime();
        queueNextRefresh(nextRefreshDelay);
        setPlayPaseButtonsImage();
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

        Log.d(TAG, "-->OnDestroy()");

        mFragmentServiceMediator.unbindFromService(getActivity(), this);
        mHandler.removeCallbacksAndMessages(null); //absolutely necessary to avoid leaks
    }

    private void refreshTrackInfoAndTotalTime() {
        String trackName = mFragmentServiceMediator.getTrackName();
        String artistName = mFragmentServiceMediator.getArtistName();
        long duration = mFragmentServiceMediator.getTrackDuration();

        if ((trackName != null) && (artistName != null) && (duration > 0)) {
            mTrackName.setText(trackName);
            mArtistName.setText(artistName);
            mTotalTime.setText(TimeUtils.makeTimeString(getActivity(),
                    duration / SEEK_BAR_MAX));
        } else {
            refreshViewsOnError();
        }
    }

    private void refreshViewsOnError() {
        mTrackName.setText("");
        mArtistName.setText("");
        mTotalTime.setText("--:--");
        mSeekBar.setProgress(0);
        mCurrentTime.setText("--:--");
        //TODO: would be great to go to next song in this case
    }

    private long refreshSeekBarAndCurrentTime() {
        long position = mFragmentServiceMediator.getPlayingPosition();
        long duration = mFragmentServiceMediator.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (SEEK_BAR_MAX * position / duration);
            mSeekBar.setProgress(progress);
            mCurrentTime.setText(TimeUtils.makeTimeString(getActivity(),
                    position / SEEK_BAR_MAX));
            return getSmoothRefreshTime(position, duration);
        } else {
            refreshViewsOnError();
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

    @Override
    public void ServiceConnected() {
        refreshTrackInfoAndTotalTime();
        setPlayPaseButtonsImage();
    }

    @Override
    public void ServiceDisconnected() {
        refreshViewsOnError();
        setPlayPaseButtonsImage();
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                long duration = mFragmentServiceMediator.getTrackDuration();
                if(duration > 0) {
                    long newSeekPostion = duration * progress / SEEK_BAR_MAX;
                    mFragmentServiceMediator.seek(newSeekPostion);
                    refreshSeekBarAndCurrentTime();
                } else {
                    //TODO: I think better will be finish app and let user restart it
                }
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private View.OnClickListener mRepeatListener = new View.OnClickListener() {
        public void onClick(View v) {
            setRepeatMode();
        }
    };

    private void setRepeatMode() {
        int mode = mFragmentServiceMediator.getRepeatMode();
        //if(mode != -1)
        if (mode == Playlist.REPEAT_NONE) {
            mFragmentServiceMediator.setRepeatMode(Playlist.REPEAT_ALL);
            Toast.makeText(getActivity(), R.string.repeat_all_notification, Toast.LENGTH_SHORT).show();
        } else if (mode == Playlist.REPEAT_ALL) {
            mFragmentServiceMediator.setRepeatMode(Playlist.REPEAT_CURRENT);
//            if (mService.getShuffleMode() != Playlist.SHUFFLE_NONE) {
//                mService.setShuffleMode(Playlist.SHUFFLE_NONE);
//                setShuffleButtonImage();
//            }
            Toast.makeText(getActivity(), R.string.repeat_current_notification, Toast.LENGTH_SHORT).show();
        } else {
            mFragmentServiceMediator.setRepeatMode(Playlist.REPEAT_NONE);
            Toast.makeText(getActivity(), R.string.repeat_off_notification, Toast.LENGTH_SHORT).show();
        }
        setRepeatButtonImage();
    }

    private void setRepeatButtonImage(){}


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
            } else if (action.equals(MediaPlaybackService.PLAYSTATE_CHANGED)) {
                setPlayPaseButtonsImage();
            }
        }
    };
}
