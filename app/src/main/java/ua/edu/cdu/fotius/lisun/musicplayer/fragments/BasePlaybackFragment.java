package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceStateChangesObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.service.ServiceStateReceiver;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnForwardListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnPlayPauseClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnRewindListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnSeekBarChangeListener;
import ua.edu.cdu.fotius.lisun.musicplayer.views.ArtistNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.views.PlayPauseButton;
import ua.edu.cdu.fotius.lisun.musicplayer.images_loader.ImageLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.images_loader.ImageViewForLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public abstract class BasePlaybackFragment extends Fragment implements ServiceConnectionObserver,
        OnRewindListener.RewindClickedListener, OnForwardListener.ForwardClickedListener, ServiceStateChangesObserver{

    protected final long ERROR_REFRESH_DELAY_IN_MILLIS = -1;
    protected final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    protected MediaPlaybackServiceWrapper mServiceWrapper;

    private ImageViewForLoader mAlbumArt;
    private PlayPauseButton mPlayButton;
    private TextView mTrackTitle;
    private ArtistNameTextView mArtistTitle;
    protected ProgressBar mProgressBar;
    private ImageLoader mImageLoader;

    private ServiceStateReceiver mServiceStateReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
        mImageLoader = new ImageLoader(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutID(), container, false);
        initPlayButton(v);
        initAlbumArtView(v);
        initTrackTitleView(v);
        initArtistTitleView(v);
        initSeekBar(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshPlaybackInfoViews();
    }

    private void initPlayButton(View parent) {
        mPlayButton = (PlayPauseButton) parent.findViewById(R.id.play);
        if(mPlayButton == null) {
            throw new RuntimeException(
                    "Your layout must have a PlayPauseButton " +
                            "whose id attribute is " +
                            "'R.id.play'");
        }
        mPlayButton.setOnClickListener(new OnPlayPauseClickListener(mServiceWrapper));
    }

    private void initAlbumArtView(View parent) {
        mAlbumArt = (ImageViewForLoader) parent.findViewById(R.id.album_art);
        if(mAlbumArt == null) {
            throw new RuntimeException(
                    "Your layout must have a ImageViewForLoader " +
                            "whose id attribute is " +
                            "'R.id.album_art'");
        }
    }

    private void initTrackTitleView(View parent) {
        mTrackTitle = (TextView) parent.findViewById(R.id.track_title);
        if(mTrackTitle == null) {
            throw new RuntimeException(
                    "Your layout must have a TextView " +
                            "whose id attribute is " +
                            "'R.id.track_title'");
        }
    }

    private void initArtistTitleView(View parent) {
        mArtistTitle = (ArtistNameTextView) parent.findViewById(R.id.artist_name);
        if(mArtistTitle == null) {
            throw new RuntimeException(
                    "Your layout must have a ArtistNameTextView " +
                            "whose id attribute is " +
                            "'R.id.artist_name'");
        }
    }

    private void initSeekBar(View parent) {
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progress_bar);
        if(mProgressBar == null) {
            throw new RuntimeException(
                    "Your layout must have a ProgressBar " +
                            "whose id attribute is " +
                            "'R.id.progress_bar'");
        }
        mProgressBar.setMax(OnSeekBarChangeListener.SEEK_BAR_MAX);
        styleProgressBar(mProgressBar);
    }

    public abstract void styleProgressBar(ProgressBar bar);

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        mServiceStateReceiver = new ServiceStateReceiver(this);
        getActivity().registerReceiver(mServiceStateReceiver, actionFilter);

        long nextRefreshDelay = refreshRepeatedlyUpdateableViews();
        queueNextRefresh(nextRefreshDelay);
        refreshPlayPauseButtonImage();
    }

    protected long refreshRepeatedlyUpdateableViews() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (OnSeekBarChangeListener.SEEK_BAR_MAX * position / duration);
            mProgressBar.setProgress(progress);
            return getSmoothRefreshTime(position, duration);
        } else {
            refreshViewsOnError();
            return ERROR_REFRESH_DELAY_IN_MILLIS;
        }
    }

    private long getSmoothRefreshTime(final long position, final long duration) {
        // calculate the number of milliseconds until the next full second, so
        // the counter can be updated at just the right time
        long remaining = TimeUtils.MILLIS_IN_SECOND -
                (position % TimeUtils.MILLIS_IN_SECOND);
        // approximate how often we would need to refresh the slider to
        // move it smoothly
        int width = mProgressBar.getWidth();
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

    private void refreshPlayPauseButtonImage() {
        boolean isPlaying = mServiceWrapper.isPlaying();
        mPlayButton.updateStateImage(isPlaying);
    }

    protected void refreshViewsOnError() {
        mTrackTitle.setText("");
        mArtistTitle.setName("");
        mProgressBar.setProgress(0);
        //this will set default image
        mImageLoader.load(MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE).withDefault().into(mAlbumArt);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mServiceStateReceiver);
        mHandler.removeMessages(REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceWrapper.unbindFromService(getActivity(), this);
        mHandler.removeCallbacksAndMessages(null); //absolutely necessary to avoid leaks
    }

    public abstract int getLayoutID();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    long nextRefreshDelay = refreshRepeatedlyUpdateableViews();
                    queueNextRefresh(nextRefreshDelay);
                    break;
            }
        }
    };

    @Override
    public void onMetadataChanged() {
        refreshPlaybackInfoViews();
        queueNextRefresh(1);
    }

    @Override
    public void onPlaybackStateChanged() {
        refreshPlayPauseButtonImage();
    }

    protected boolean refreshPlaybackInfoViews() {
        String trackName = mServiceWrapper.getTrackName();
        String artistName = mServiceWrapper.getArtistName();
        long albumID = mServiceWrapper.getAlbumID();

        if ((trackName == null) || (artistName == null)
                || (albumID == MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE)) {
            refreshViewsOnError();
            return false;
        }

        if(mTrackTitle != null) {
            mTrackTitle.setText(trackName);
        }

        if(mArtistTitle != null) {
            mArtistTitle.setName(artistName);
        }

        if(mAlbumArt != null) {
            mImageLoader.load(albumID).withDefault().into(mAlbumArt);
        }

        return true;
    }

    @Override
    public void ServiceConnected() {
        refreshPlaybackInfoViews();
        refreshPlayPauseButtonImage();
    }

    @Override
    public void ServiceDisconnected() {
        refreshViewsOnError();
        refreshPlayPauseButtonImage();
    }

    @Override
    public void onForwardClicked() {
        refreshRepeatedlyUpdateableViews();
    }

    @Override
    public void onRewindClicked() {
        refreshRepeatedlyUpdateableViews();
    }
}
