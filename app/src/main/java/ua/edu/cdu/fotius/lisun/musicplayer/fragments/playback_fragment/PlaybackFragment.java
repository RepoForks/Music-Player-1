package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.ArtistNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.BaseNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public class PlaybackFragment extends Fragment implements ServiceConnectionObserver,
        ListenerCallbacks {

    private final String TAG = getClass().getSimpleName();

    private final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    private TextView mTrackName;
    private BaseNameTextView mArtistName;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private PlayButton mPlayButton;
    private PlayButton mPlayAdditionalButton;
    //TODO: collapseable --> minimal
    private RepeatButton mRepeatButton;
    private ShuffleButton mShuffleButton;
    private MediaPlaybackServiceWrapper mServiceWrapper;

    public PlaybackFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_playback, container, false);

        ViewsFactory viewsFactory = new ViewsFactory(v, this);
        viewsFactory.initializePrevButton();
        viewsFactory.initializePrevAdditionalButton();
        viewsFactory.initializeNextButton();
        viewsFactory.initializeNextAdditionalButton();
        mPlayButton = viewsFactory.initializePlayPauseButton();
        mPlayAdditionalButton = viewsFactory.initializePlayPauseAdditionalButton();
        mRepeatButton = viewsFactory.initializeRepeatButton();
        mShuffleButton = viewsFactory.initializeShuffleButton();
        mTrackName = viewsFactory.initializeTrackNameView();
        mArtistName = viewsFactory.initializeArtistNameView();
        mSeekBar = viewsFactory.initializeSeekBar();
        mCurrentTime = viewsFactory.initializeCurrentTimeView();
        mTotalTime = viewsFactory.initializeTotalTimeView();

        return v;
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
        setPlayPauseButtonsImage();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    private void setPlayPauseButtonsImage() {
        boolean isPlaying = mServiceWrapper.isPlaying();
        mPlayButton.setImage(isPlaying);
        mPlayAdditionalButton.setImage(isPlaying);
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
        mServiceWrapper.unbindFromService(getActivity(), this);
        mHandler.removeCallbacksAndMessages(null); //absolutely necessary to avoid leaks
    }

    private void refreshTrackInfoAndTotalTime() {
        String trackName = mServiceWrapper.getTrackName();
        String artistName = mServiceWrapper.getArtistName();
        long duration = mServiceWrapper.getTrackDuration();

        if ((trackName != null) && (artistName != null) && (duration > 0)) {
            mTrackName.setText(trackName);
            mArtistName.setName(artistName);
            mTotalTime.setText(TimeUtils.makeTimeString(getActivity(),
                    duration / ViewsFactory.SEEK_BAR_MAX));
        } else {
            refreshViewsOnError();
        }
    }

    private void refreshViewsOnError() {
        mTrackName.setText("");
        mArtistName.setName("");
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
    public void setPlayPauseButtonsImageCallback() {
        setPlayPauseButtonsImage();
    }

    @Override
    public void setRepeatButtonImageCallback() {
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
    }

    @Override
    public void setShuffleButtonImageCallback() {
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    @Override
    public long refreshSeekBarAndCurrentTimeCallback() {
        return refreshSeekBarAndCurrentTime();
    }

    private long refreshSeekBarAndCurrentTime() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (ViewsFactory.SEEK_BAR_MAX * position / duration);
            mSeekBar.setProgress(progress);
            mCurrentTime.setText(TimeUtils.makeTimeString(getActivity(),
                    position / ViewsFactory.SEEK_BAR_MAX));
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
        setPlayPauseButtonsImage();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    @Override
    public void ServiceDisconnected() {
        refreshViewsOnError();
        setPlayPauseButtonsImage();
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
                setPlayPauseButtonsImage();
            }
        }
    };
}
