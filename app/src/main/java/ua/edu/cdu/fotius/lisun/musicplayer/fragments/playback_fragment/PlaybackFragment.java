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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ConcealableImageView;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.BaseNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.PlayPauseButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ShuffleButton;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public class PlaybackFragment extends Fragment implements ServiceConnectionObserver, PlaybackViewsStateListener {

    private final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    private ImageView mAlbumArt;
    private ConcealableImageView mConcealableAlbumArtImageView;
    private TextView mTrackName;
    private BaseNameTextView mArtistName;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private PlayPauseButton mPlayButton;
    private PlayPauseButton mConcealablePlayPauseButton;
    private RepeatButton mRepeatButton;
    private ShuffleButton mShuffleButton;
    private MediaPlaybackServiceWrapper mServiceWrapper;
    private ImageLoader mImageLoader;
    private LinearLayout mDragView;

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

        PlaybackFragmentViewsCreator playbackFragmentViewsCreator =
                new PlaybackFragmentViewsCreator(v, mServiceWrapper, this);
        playbackFragmentViewsCreator.createPrevButton();
        playbackFragmentViewsCreator.createPrevConcealableButton();
        playbackFragmentViewsCreator.createNextButton();
        playbackFragmentViewsCreator.createNextConcealableButton();
        mAlbumArt = playbackFragmentViewsCreator.createAlbumArtImageView();
        mConcealableAlbumArtImageView = playbackFragmentViewsCreator.createConcealableAlbumArtImageView();
        mPlayButton = playbackFragmentViewsCreator.createPlayPauseButton();
        mConcealablePlayPauseButton = playbackFragmentViewsCreator.createPlayPauseConcealableButton();
        mRepeatButton = playbackFragmentViewsCreator.createRepeatButton();
        mShuffleButton = playbackFragmentViewsCreator.createShuffleButton();
        mTrackName = playbackFragmentViewsCreator.createTrackNameView();
        mArtistName = playbackFragmentViewsCreator.createArtistNameView();
        mSeekBar = playbackFragmentViewsCreator.createSeekBar();
        mCurrentTime = playbackFragmentViewsCreator.createCurrentTimeView();
        mTotalTime = playbackFragmentViewsCreator.createTotalTimeView();
        mDragView = (LinearLayout)v.findViewById(R.id.minimal_layout_container);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        getActivity().registerReceiver(mStatusListener, actionFilter);
        long nextRefreshDelay = updateSeekBarAndCurrentTime();
        queueNextRefresh(nextRefreshDelay);
        updPlayPauseButtonImage();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    private void updPlayPauseButtonImage() {
        boolean isPlaying = mServiceWrapper.isPlaying();
        mPlayButton.updateStateImage(isPlaying);
        mConcealablePlayPauseButton.updateStateImage(isPlaying);
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
                    duration / PlaybackFragmentViewsCreator.SEEK_BAR_MAX));
        } else {
            updateViewsOnError();
        }

        long albumID = mServiceWrapper.getAlbumID();
        if(albumID != MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE) {
            mImageLoader.load(albumID).withDefault(R.drawable.default_album_art_512dp).into(mAlbumArt);
            mConcealableAlbumArtImageView.setAlbumArt(albumID);
        }
    }

    private void updateViewsOnError() {
        mTrackName.setText("");
        mArtistName.setName("");
        mTotalTime.setText("--:--");
        mSeekBar.setProgress(0);
        mCurrentTime.setText("--:--");
        //TODO: would be great to go to next song in this case
    }

    @Override
    public long updateSeekBarAndCurrentTime() {
        return updSeekBarAndTime();
    }

    @Override
    public void updatePlayPauseButtonImage() {
        updPlayPauseButtonImage();
    }

    @Override
    public void updateRepeatButtonImage() {
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
    }

    @Override
    public void updateShuffleButtonImage() {
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    private long updSeekBarAndTime() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (PlaybackFragmentViewsCreator.SEEK_BAR_MAX * position / duration);
            mSeekBar.setProgress(progress);
            mCurrentTime.setText(TimeUtils.makeTimeString(getActivity(),
                    position / PlaybackFragmentViewsCreator.SEEK_BAR_MAX));
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
        updPlayPauseButtonImage();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }

    @Override
    public void ServiceDisconnected() {
        updateViewsOnError();
        updPlayPauseButtonImage();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    long nextRefreshDelay = updateSeekBarAndCurrentTime();
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
                updPlayPauseButtonImage();
            }
        }
    };
}
