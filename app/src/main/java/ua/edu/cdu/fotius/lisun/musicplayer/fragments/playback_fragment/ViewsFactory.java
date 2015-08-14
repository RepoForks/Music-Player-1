package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ViewsFactory {

    public static final int SEEK_BAR_MAX = 1000;

    private View mLayout;
    private ListenerCallbacks mCallbacks;
    private OnRewindListener mRewindListener;
    private OnPreviousClickedListener mPrevListener;
    private OnFastForwardListener mForwardListener;
    private OnNextClickedListener mNextListener;
    private OnPlayPauseClickedListener mPlayPauseListener;


    public ViewsFactory(View layout, ListenerCallbacks callbacks){
        mLayout = layout;
        mCallbacks = callbacks;

         mRewindListener = new OnRewindListener(mCallbacks);
         mPrevListener = new OnPreviousClickedListener(mCallbacks);
         mForwardListener = new OnFastForwardListener(mCallbacks);
         mNextListener = new OnNextClickedListener(mCallbacks);
        mPlayPauseListener = new OnPlayPauseClickedListener(mCallbacks);
    }

    public ImageButton initializePrevButton() {
        RepeatingButton prevButton =
                (RepeatingButton) mLayout.findViewById(R.id.prev);
        prevButton.setOnClickListener(mPrevListener);
        prevButton.setRepeatListener(mRewindListener);

        return prevButton;
    }

    public ImageButton initializePrevAdditionalButton() {
        RepeatingButton prevAdditionalButton =
                (RepeatingButton) mLayout.findViewById(R.id.prev_additional);
        prevAdditionalButton.setOnClickListener(mPrevListener);
        prevAdditionalButton.setRepeatListener(mRewindListener);

        return prevAdditionalButton;
    }

    public ImageButton initializeNextButton() {
        RepeatingButton nextButton =
                (RepeatingButton) mLayout.findViewById(R.id.next);
        nextButton.setOnClickListener(mNextListener);
        nextButton.setRepeatListener(mForwardListener);

        return nextButton;
    }

    public ImageButton initializeNextAdditionalButton() {
        RepeatingButton nextAdditionalButton =
                (RepeatingButton) mLayout.findViewById(R.id.next_additional);
        nextAdditionalButton.setOnClickListener(mNextListener);
        nextAdditionalButton.setRepeatListener(mForwardListener);

        return nextAdditionalButton;
    }

    public PlayButton initializePlayPauseButton() {
        PlayButton playButton = (PlayButton) mLayout.findViewById(R.id.play);
        playButton.setOnClickListener(new OnPlayPauseClickedListener(mCallbacks));

        return playButton;
    }

    public PlayButton initializePlayPauseAdditionalButton() {
        PlayButton playPauseAdditionalButton = (PlayButton) mLayout.findViewById(R.id.play_additional);
        playPauseAdditionalButton.setOnClickListener(mPlayPauseListener);

        return playPauseAdditionalButton;
    }

    public RepeatButton initializeRepeatButton() {
        RepeatButton repeatButton = (RepeatButton) mLayout.findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new OnRepeatClickedListener(mCallbacks));

        return repeatButton;
    }

    public ShuffleButton initializeShuffleButton() {
        ShuffleButton shuffleButton = (ShuffleButton) mLayout.findViewById(R.id.shuffle);
        shuffleButton.setOnClickListener(new OnShuffleClickedListener(mCallbacks));

        return shuffleButton;
    }

    public TextView initializeTrackNameView() {
        TextView trackName = (TextView) mLayout.findViewById(R.id.track_title);
        return trackName;
    }

    public TextView initializeArtistNameView() {
        TextView artistName = (TextView) mLayout.findViewById(R.id.artist_name);
        return artistName;
    }

    public SeekBar initializeSeekBar() {
        SeekBar seekBar = (SeekBar) mLayout.findViewById(R.id.seek_bar);
        seekBar.setMax(SEEK_BAR_MAX);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangedListener(mCallbacks, SEEK_BAR_MAX));

        return seekBar;
    }

    public TextView initializeCurrentTimeView() {
        TextView currentTime = (TextView) mLayout.findViewById(R.id.current_time);
        return currentTime;
    }

    public TextView initializeTotalTimeView() {
        TextView totalTime = (TextView) mLayout.findViewById(R.id.total_time);
        return totalTime;
    }
}
