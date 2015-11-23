package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ArtistNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.BaseNameTextView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ConcealableImageView;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.LoopingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.PlayPauseButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ShuffleButton;

public class PlaybackFragmentViewsCreator {

    public static final int SEEK_BAR_MAX = 1000;

    private View mLayout;
    private MediaPlaybackServiceWrapper mPlaybackServiceWrapper;
    private PlaybackViewsStateListener mPlaybackViewsStateListener;
    private OnRewindListener mRewindListener;
    private OnPreviousClickListener mPrevListener;
    private OnFastForwardListener mForwardListener;
    private OnNextClickListener mNextListener;
    private OnPlayPauseClickListener mPlayPauseListener;


    public PlaybackFragmentViewsCreator(View layout, MediaPlaybackServiceWrapper serviceWrapper,
                                        PlaybackViewsStateListener playbackViewsStateListener) {
        mLayout = layout;
        mPlaybackServiceWrapper = serviceWrapper;
        mPlaybackViewsStateListener = playbackViewsStateListener;

        mRewindListener = new OnRewindListener(mPlaybackServiceWrapper, mPlaybackViewsStateListener);
        mPrevListener = new OnPreviousClickListener(mPlaybackServiceWrapper);
        mForwardListener = new OnFastForwardListener(mPlaybackServiceWrapper, mPlaybackViewsStateListener);
        mNextListener = new OnNextClickListener(mPlaybackServiceWrapper);
        mPlayPauseListener = new OnPlayPauseClickListener(mPlaybackServiceWrapper, mPlaybackViewsStateListener);
    }

    public ImageButton createPrevButton() {
        LoopingImageButton prevButton =
                (LoopingImageButton) mLayout.findViewById(R.id.prev);
        prevButton.setOnClickListener(mPrevListener);
        prevButton.setRepeatListener(mRewindListener);

        return prevButton;
    }

    public ImageButton createPrevConcealableButton() {
        LoopingImageButton prevAdditionalButton =
                (LoopingImageButton) mLayout.findViewById(R.id.prev_additional);
        prevAdditionalButton.setOnClickListener(mPrevListener);
        prevAdditionalButton.setRepeatListener(mRewindListener);

        return prevAdditionalButton;
    }

    public ImageButton createNextButton() {
        LoopingImageButton nextButton =
                (LoopingImageButton) mLayout.findViewById(R.id.next);
        nextButton.setOnClickListener(mNextListener);
        nextButton.setRepeatListener(mForwardListener);

        return nextButton;
    }

    public ImageButton createNextConcealableButton() {
        LoopingImageButton nextAdditionalButton =
                (LoopingImageButton) mLayout.findViewById(R.id.next_additional);
        nextAdditionalButton.setOnClickListener(mNextListener);
        nextAdditionalButton.setRepeatListener(mForwardListener);

        return nextAdditionalButton;
    }

    public ConcealableImageView createConcealableAlbumArtImageView() {
        return (ConcealableImageView) mLayout.findViewById(R.id.concealable_album_art);
    }

    public ImageView createAlbumArtImageView() {
        return (ImageView) mLayout.findViewById(R.id.album_art);
    }

    public PlayPauseButton createPlayPauseButton() {
        PlayPauseButton playButton = (PlayPauseButton) mLayout.findViewById(R.id.play);
        playButton.setOnClickListener(mPlayPauseListener);
        return playButton;
    }

    public PlayPauseButton createPlayPauseConcealableButton() {
        PlayPauseButton playPauseAdditionalButton = (PlayPauseButton) mLayout.findViewById(R.id.play_additional);
        playPauseAdditionalButton.setOnClickListener(mPlayPauseListener);
        return playPauseAdditionalButton;
    }

    public RepeatButton createRepeatButton() {
        RepeatButton repeatButton = (RepeatButton) mLayout.findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new OnRepeatClickListener(mPlaybackServiceWrapper,
                mPlaybackViewsStateListener));
        return repeatButton;
    }

    public ShuffleButton createShuffleButton() {
        ShuffleButton shuffleButton = (ShuffleButton) mLayout.findViewById(R.id.shuffle);
        shuffleButton.setOnClickListener(new OnShuffleClickListener(mPlaybackServiceWrapper,
                mPlaybackViewsStateListener));
        return shuffleButton;
    }

    public TextView createTrackNameView() {
        TextView trackName = (TextView) mLayout.findViewById(R.id.track_title);
        return trackName;
    }

    public BaseNameTextView createArtistNameView() {
        BaseNameTextView artistName = (ArtistNameTextView) mLayout.findViewById(R.id.artist_name);
        return artistName;
    }

    public SeekBar createSeekBar() {
        SeekBar seekBar = (SeekBar) mLayout.findViewById(R.id.seek_bar);
        seekBar.setMax(SEEK_BAR_MAX);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(mPlaybackServiceWrapper,
                mPlaybackViewsStateListener, SEEK_BAR_MAX));
        return seekBar;
    }

    public TextView createCurrentTimeView() {
        TextView currentTime = (TextView) mLayout.findViewById(R.id.current_time);
        return currentTime;
    }

    public TextView createTotalTimeView() {
        TextView totalTime = (TextView) mLayout.findViewById(R.id.total_time);
        return totalTime;
    }
}
