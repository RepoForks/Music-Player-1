package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


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
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.RepeatingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.service_stuff.Playlist;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public class PlaybackFragment extends Fragment implements ServiceConnectionObserver,
        BaseRewindFastForwardListener.RewindForwardListenerCallbacks, OnNextClickedListener.OnNextClickedListenerCallbacks,
        OnPreviousClickedListener.OnPreviousClickedListenerCallbacks, OnSeekBarChangedListener.OnSeekBarChangedCallbacks,
        BaseRepeatShuffleClickedListener.Callbacks, OnPlayPauseClickedListener.Callbacks {

    private final String TAG = getClass().getSimpleName();

    private final int SEEK_BAR_MAX = 1000;
    private final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    //private ServiceInterface mFragmentServiceMediator;

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
    private ImageButton mShuffleButton;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    public PlaybackFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       // mFragmentServiceMediator = (ServiceInterface) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_playback, container, false);
        initializePrevButtons(v);
        initializeNextButtons(v);
        initializePlayPauseButtons(v);
        initializeRepeatAndShuffleButton(v);
        initializeTrackInfoViews(v);
        return v;
    }

    private void initializePrevButtons(View layout) {
        RewindListener rewindListener = new RewindListener(this);
        OnPreviousClickedListener prevListener = new OnPreviousClickedListener(this);

        RepeatingImageButton prevAdditionalButton =
                (RepeatingImageButton) layout.findViewById(R.id.prev_additional);
        prevAdditionalButton.setOnClickListener(prevListener);
        prevAdditionalButton.setRepeatListener(rewindListener);

        RepeatingImageButton prevButton =
                (RepeatingImageButton) layout.findViewById(R.id.prev);
        prevButton.setOnClickListener(prevListener);
        prevButton.setRepeatListener(rewindListener);
    }

    private void initializeNextButtons(View layout) {
        FastForwardListener forwardListener = new FastForwardListener(this);
        OnNextClickedListener nextCallbacks = new OnNextClickedListener(this);

        RepeatingImageButton nextAdditionalButton =
                (RepeatingImageButton) layout.findViewById(R.id.next_additional);
        nextAdditionalButton.setOnClickListener(nextCallbacks);
        nextAdditionalButton.setRepeatListener(forwardListener);

        RepeatingImageButton nextButton =
                (RepeatingImageButton) layout.findViewById(R.id.next);
        nextButton.setOnClickListener(nextCallbacks);
        nextButton.setRepeatListener(forwardListener);
    }

    private void initializePlayPauseButtons(View layout) {
        mPlayPauseAdditionalButton = (ImageButton) layout.findViewById(R.id.pause_additional);
        mPlayPauseAdditionalButton.setOnClickListener(new OnPlayPauseClickedListener(this));
        mPlayPauseButton = (ImageButton) layout.findViewById(R.id.pause);
        mPlayPauseButton.setOnClickListener(new OnPlayPauseClickedListener(this));
    }

    private void initializeRepeatAndShuffleButton(View layout) {
        mRepeatButton = (ImageButton) layout.findViewById(R.id.repeat);
        mShuffleButton = (ImageButton) layout.findViewById(R.id.shuffle);
        mRepeatButton.setOnClickListener(new OnRepeatClickedListener(this));
        mShuffleButton.setOnClickListener(new OnShuffleClickedListener(this));
    }

    private void initializeTrackInfoViews(View layout) {
        mTrackName = (TextView) layout.findViewById(R.id.track_title);
        mArtistName = (TextView) layout.findViewById(R.id.artist_name);

        mSeekBar = (SeekBar) layout.findViewById(R.id.seek_bar);
        mSeekBar.setMax(SEEK_BAR_MAX);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangedListener(this, SEEK_BAR_MAX));

        mCurrentTime = (TextView) layout.findViewById(R.id.current_time);
        mTotalTime = (TextView) layout.findViewById(R.id.total_time);
    }

    public void setPlayPaseButtonsImage() {
        if (mServiceWrapper.isPlaying()) {
            mPlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            mPlayPauseAdditionalButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
            mPlayPauseAdditionalButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    public void setRepeatButtonImage() {
        Log.d(TAG, "--> setRepeatButtonImage() --> mode: " + mServiceWrapper.getRepeatMode());
        switch (mServiceWrapper.getRepeatMode()) {
            case Playlist.REPEAT_ALL:
                mRepeatButton.setImageResource(R.mipmap.ic_repeat_all_btn);
                break;
            case Playlist.REPEAT_CURRENT:
                mRepeatButton.setImageResource(R.mipmap.ic_repeat_once_btn);
                break;
            case Playlist.REPEAT_NONE:
                mRepeatButton.setImageResource(R.mipmap.ic_repeat_off_btn);
                break;
            default:
                mRepeatButton.setImageResource(R.mipmap.ic_repeat_off_btn);
                break;
        }
    }

    public void setShuffleButtonImage() {
        Log.d(TAG, "--> setShuffleButtonImage() --> mode: " + mServiceWrapper.getShuffleMode());
        switch (mServiceWrapper.getShuffleMode()) {
            case Playlist.SHUFFLE_NONE:
                mShuffleButton.setImageResource(R.mipmap.ic_shuffle_off_btn);
                break;
            case Playlist.SHUFFLE_AUTO:
                mShuffleButton.setImageResource(R.mipmap.ic_partyshuffle_on_btn);
                break;
            case Playlist.SHUFFLE_NORMAL:
                mShuffleButton.setImageResource(R.mipmap.ic_shuffle_on_btn);
                break;
            default:
                mShuffleButton.setImageResource(R.mipmap.ic_shuffle_off_btn);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "--> onStart()");
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        getActivity().registerReceiver(mStatusListener, actionFilter);
        long nextRefreshDelay = refreshSeekBarAndCurrentTime();
        queueNextRefresh(nextRefreshDelay);
        setPlayPaseButtonsImage();
        setRepeatButtonImage();
        setShuffleButtonImage();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "--> onStop()");
        getActivity().unregisterReceiver(mStatusListener);
        mHandler.removeMessages(REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindFromService(getActivity(), this);
        mHandler.removeCallbacksAndMessages(null); //absolutely necessary to avoid leaks
    }

    private void refreshTrackInfoAndTotalTime() {
        String trackName = mServiceWrapper.getTrackName();
        String artistName = mServiceWrapper.getArtistName();
        long duration = mServiceWrapper.getTrackDuration();

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

    @Override
    public long getPlayingPosition() {
        return mServiceWrapper.getPlayingPosition();
    }

    @Override
    public long getTrackDuration() {
        return mServiceWrapper.getTrackDuration();
    }

    @Override
    public void goToNextTrack() {
        mServiceWrapper.next();
    }

    @Override
    public void goToPreviousTrack() {
        mServiceWrapper.prev();
    }

    @Override
    public void seek(long position) {
        mServiceWrapper.seek(position);
    }

    @Override
    public void play() {
        mServiceWrapper.play();
    }

    @Override
    public void pause() {
        mServiceWrapper.pause();
    }

    @Override
    public boolean isPlaying() {
        return mServiceWrapper.isPlaying();
    }

    @Override
    public int getRepeatMode() {
        return mServiceWrapper.getRepeatMode();
    }

    @Override
    public void setShuffleMode(int shuffleMode) {
        mServiceWrapper.setShuffleMode(shuffleMode);
    }

    @Override
    public void setRepeatMode(int repeatMode) {
        mServiceWrapper.setRepeatMode(repeatMode);
    }

    @Override
    public int getShuffleMode() {
        return mServiceWrapper.getShuffleMode();
    }

    @Override
    public long refreshSeekBarAndCurrentTime() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
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
        setRepeatButtonImage();
        setShuffleButtonImage();
    }

    @Override
    public void ServiceDisconnected() {
        refreshViewsOnError();
        setPlayPaseButtonsImage();
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
                setPlayPaseButtonsImage();
            }
        }
    };
}
