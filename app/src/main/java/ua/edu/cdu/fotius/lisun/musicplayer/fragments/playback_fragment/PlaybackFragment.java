package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditTrackInfoFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ArtistNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.BaseNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.LoopingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.PlayPauseButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ShuffleButton;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public class PlaybackFragment extends Fragment implements ServiceConnectionObserver, OnForwardListener.ForwardClickedListener,
        OnRewindListener.RewindClickedListener, OnShuffleClickListener.ShuffleClickedListener,
        OnRepeatClickListener.RepeatClickedListener,
        OnSeekBarChangeListener.SeekBarProgressChangedListener{

    private final String TAG = getClass().getSimpleName();

    private final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    private ImageView mAlbumArt;
    private TextView mTrackTitle;
    private BaseNameTextView mArtistName;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private PlayPauseButton mPlayButton;
    private RepeatButton mRepeatButton;
    private ShuffleButton mShuffleButton;
    private MediaPlaybackServiceWrapper mServiceWrapper;
    private ImageLoader mImageLoader;

    public PlaybackFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);

        mImageLoader = new ImageLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_playback, container, false);

        LoopingImageButton prevButton =
                (LoopingImageButton) v.findViewById(R.id.prev);
        prevButton.setOnClickListener(new OnPreviousClickListener(mServiceWrapper));
        prevButton.setRepeatListener(new OnRewindListener(mServiceWrapper, this));

        LoopingImageButton nextButton =
                (LoopingImageButton) v.findViewById(R.id.next);
        nextButton.setOnClickListener(new OnNextClickListener(mServiceWrapper));
        nextButton.setRepeatListener(new OnRewindListener(mServiceWrapper, this));

        mAlbumArt = (ImageView) v.findViewById(R.id.album_art);
        mPlayButton = (PlayPauseButton) v.findViewById(R.id.play);
        mPlayButton.setOnClickListener(new OnPlayPauseClickListener(mServiceWrapper));

        mRepeatButton = (RepeatButton) v.findViewById(R.id.repeat);
        mRepeatButton.setOnClickListener(new OnRepeatClickListener(mServiceWrapper, this));

        mShuffleButton = (ShuffleButton) v.findViewById(R.id.shuffle);
        mShuffleButton.setOnClickListener(new OnShuffleClickListener(mServiceWrapper, this));
        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mArtistName = (ArtistNameTextView) v.findViewById(R.id.artist_name);

        mSeekBar = (SeekBar) v.findViewById(R.id.seek_bar);
        mSeekBar.setMax(OnSeekBarChangeListener.SEEK_BAR_MAX);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(mServiceWrapper, this));

        mCurrentTime = (TextView) v.findViewById(R.id.current_time);
        mTotalTime = (TextView) v.findViewById(R.id.total_time);

        Button button = (Button) v.findViewById(R.id.show_current_queue);
        button.setOnClickListener(new OnShowQueueClickListener(getActivity(), mServiceWrapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        getActivity().registerReceiver(mServiceStatusListener, actionFilter);

        IntentFilter trackInfoChangedFilter = new IntentFilter();
        trackInfoChangedFilter.addAction(EditTrackInfoFragment.ACTION_TRACK_INFO_CHANGED);
        getActivity().registerReceiver(mTrackInfoChangedListener, trackInfoChangedFilter);

        long nextRefreshDelay = updateSeekBarAndTime();
        queueNextRefresh(nextRefreshDelay);
        updatePlayPauseButtonImage();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    private void updatePlayPauseButtonImage() {
        boolean isPlaying = mServiceWrapper.isPlaying();
        mPlayButton.updateStateImage(isPlaying);
    }

    @Override
    public void onStop() {
        super.onStop();

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        getActivity().unregisterReceiver(mServiceStatusListener);
        getActivity().unregisterReceiver(mTrackInfoChangedListener);

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
            mTrackTitle.setText(trackName);
            mArtistName.setName(artistName);
            mTotalTime.setText(TimeUtils.makeTimeString(getActivity(),
                    duration / OnSeekBarChangeListener.SEEK_BAR_MAX));
        } else {
            updateViewsOnError();
        }

        long albumID = mServiceWrapper.getAlbumID();
        if(albumID != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            mImageLoader.load(albumID).withDefault(R.drawable.default_album_art_512dp).into(mAlbumArt);
        }
    }

    private void updateViewsOnError() {
        mTrackTitle.setText("");
        mArtistName.setName("");
        mTotalTime.setText("--:--");
        mSeekBar.setProgress(0);
        mCurrentTime.setText("--:--");
        //TODO: would be great to go to next song in this case
    }


    private long updateSeekBarAndTime() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (OnSeekBarChangeListener.SEEK_BAR_MAX * position / duration);
            mSeekBar.setProgress(progress);
            mCurrentTime.setText(TimeUtils.makeTimeString(getActivity(),
                    position / OnSeekBarChangeListener.SEEK_BAR_MAX));
            return getSmoothRefreshTime(position, duration);
        } else {
            updateViewsOnError();
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
        updatePlayPauseButtonImage();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    @Override
    public void ServiceDisconnected() {
        updateViewsOnError();
        updatePlayPauseButtonImage();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    long nextRefreshDelay = updateSeekBarAndTime();
                    queueNextRefresh(nextRefreshDelay);
                    break;
            }
        }
    };

    private BroadcastReceiver mServiceStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MediaPlaybackService.META_CHANGED)) {
                refreshTrackInfoAndTotalTime();
                queueNextRefresh(1);
            } else if (action.equals(MediaPlaybackService.PLAYSTATE_CHANGED)) {
                updatePlayPauseButtonImage();
            }
        }
    };

    private BroadcastReceiver mTrackInfoChangedListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(EditTrackInfoFragment.ACTION_TRACK_INFO_CHANGED)) {
                long changedInfoTrackId = intent.getLongExtra(EditTrackInfoFragment.TRACK_ID_KEY,
                        AudioStorage.WRONG_ID);
                if((changedInfoTrackId != AudioStorage.WRONG_ID)
                        && (changedInfoTrackId == mServiceWrapper.getTrackID())) {
                    mServiceWrapper.updateCurrentTrackInfo();
                    refreshTrackInfoAndTotalTime();
                }
            }
        }
    };

    @Override
    public void onForwardClicked() {
        updateSeekBarAndTime();
    }

    @Override
    public void onRewindClicked() {
        updateSeekBarAndTime();
    }

    @Override
    public void onRepeatClicked() {
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    @Override
    public void onShuffleClicked() {
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
    }

    @Override
    public void onProgressChanged() {
        updateSeekBarAndTime();
    }
}
