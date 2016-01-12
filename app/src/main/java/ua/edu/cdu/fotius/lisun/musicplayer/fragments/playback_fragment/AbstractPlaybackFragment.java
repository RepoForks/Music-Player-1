package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

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
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceConnectionObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceStateChangesObserver;
import ua.edu.cdu.fotius.lisun.musicplayer.ServiceStateReceiver;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditTrackInfoFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ArtistNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.LoopingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.PlayPauseButton;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageViewForLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public abstract class AbstractPlaybackFragment extends Fragment implements ServiceConnectionObserver,
        OnRewindListener.RewindClickedListener, OnForwardListener.ForwardClickedListener, ServiceStateChangesObserver{

    protected final long ERROR_REFRESH_DELAY_IN_MILLIS = -1;
    protected final long DEFAULT_REFRESH_DELAY_IN_MILLIS = 500;
    private final int REFRESH = 1;

    protected MediaPlaybackServiceWrapper mServiceWrapper;

    private ImageViewForLoader mAlbumArt;
    private PlayPauseButton mPlayButton;
    private TextView mTrackTitle;
    private ArtistNameTextView mArtistTitle;
    protected SeekBar mSeekBar;
    private ImageLoader mImageLoader;

    private ServiceStateReceiver mServiceStateReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mServiceWrapper = MediaPlaybackServiceWrapper.getInstance();
        mServiceWrapper.bindToService(getActivity(), this);
        mImageLoader = new ImageLoader(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutID(), container, false);
        initPrevButton(v);
        initNextButton(v);
        initPlayButton(v);
        initAlbumArtView(v);
        initTrackTitleView(v);
        initArtistTitleView(v);
        initSeekBar(v);
        return v;
    }

    private void initPrevButton(View parent) {
        LoopingImageButton prevButton =
                (LoopingImageButton) parent.findViewById(R.id.prev);
        if(prevButton == null) {
            throw new RuntimeException(
                    "Your layout must have a LoopingImageButton " +
                            "whose id attribute is " +
                            "'R.id.prev'");
        }

        prevButton.setOnClickListener(new OnPreviousClickListener(mServiceWrapper));
        prevButton.setRepeatListener(new OnRewindListener(mServiceWrapper, this));
    }

    private void initNextButton(View parent) {
        LoopingImageButton nextButton =
                (LoopingImageButton) parent.findViewById(R.id.next);
        if(nextButton == null) {
            throw new RuntimeException(
                    "Your layout must have a LoopingImageButton " +
                            "whose id attribute is " +
                            "'R.id.next'");
        }
        nextButton.setOnClickListener(new OnNextClickListener(mServiceWrapper));
        nextButton.setRepeatListener(new OnForwardListener(mServiceWrapper, this));
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
        mSeekBar = (SeekBar) parent.findViewById(R.id.seek_bar);
        if(mArtistTitle == null) {
            throw new RuntimeException(
                    "Your layout must have a SeekBar " +
                            "whose id attribute is " +
                            "'R.id.seek_bar'");
        }
        mSeekBar.setMax(OnSeekBarChangeListener.SEEK_BAR_MAX);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter actionFilter = new IntentFilter();
        actionFilter.addAction(MediaPlaybackService.META_CHANGED);
        actionFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        mServiceStateReceiver = new ServiceStateReceiver(this);
        getActivity().registerReceiver(mServiceStateReceiver, actionFilter);

        IntentFilter trackInfoChangedFilter = new IntentFilter();
        trackInfoChangedFilter.addAction(EditTrackInfoFragment.ACTION_TRACK_INFO_CHANGED);
        getActivity().registerReceiver(mTrackInfoChangedListener, trackInfoChangedFilter);

        long nextRefreshDelay = refreshRepeatedlyUpdateableViews();
        queueNextRefresh(nextRefreshDelay);
        refreshPlayPauseButtonImage();
    }

    protected long refreshRepeatedlyUpdateableViews() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
        if (position >= 0 && duration > 0) {
            int progress = (int) (OnSeekBarChangeListener.SEEK_BAR_MAX * position / duration);
            mSeekBar.setProgress(progress);
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

    private void refreshPlayPauseButtonImage() {
        boolean isPlaying = mServiceWrapper.isPlaying();
        mPlayButton.updateStateImage(isPlaying);
    }

    protected void refreshViewsOnError() {
        mTrackTitle.setText("");
        mArtistTitle.setName("");
        mSeekBar.setProgress(0);
        //this will set default image
        mImageLoader.load(MediaPlaybackServiceWrapper.ERROR_RETURN_VALUE)
                .withDefault(R.drawable.default_album_art_512dp).into(mAlbumArt);
        //TODO: maybe go to next song
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mServiceStateReceiver);
        getActivity().unregisterReceiver(mTrackInfoChangedListener);
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

        mTrackTitle.setText(trackName);
        mArtistTitle.setName(artistName);
        mImageLoader.load(albumID)
                .withDefault(R.drawable.default_album_art_512dp).into(mAlbumArt);

        return true;
    }

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
                    refreshPlaybackInfoViews();
                }
            }
        }
    };

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
