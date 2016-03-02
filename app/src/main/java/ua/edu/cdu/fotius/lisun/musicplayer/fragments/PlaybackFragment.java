package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnForwardListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnNextClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnPreviousClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnRepeatClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnRewindListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnSeekBarChangeListener;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnShuffleClickListener;
import ua.edu.cdu.fotius.lisun.musicplayer.views.LoopingImageButton;
import ua.edu.cdu.fotius.lisun.musicplayer.views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.views.ShuffleButton;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public class PlaybackFragment extends BasePlaybackFragment implements OnRepeatClickListener.RepeatClickedListener,
        OnShuffleClickListener.ShuffleClickedListener, OnSeekBarChangeListener.SeekBarProgressChangedListener{

    private TextView mCurrentTime;
    private TextView mTotalTime;
    private RepeatButton mRepeatButton;
    private ShuffleButton mShuffleButton;

    public PlaybackFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        initPrevButton(v);
        initNextButton(v);

        mRepeatButton = (RepeatButton) v.findViewById(R.id.repeat);
        mRepeatButton.setOnClickListener(new OnRepeatClickListener(mServiceWrapper, this));

        mShuffleButton = (ShuffleButton) v.findViewById(R.id.shuffle);
        mShuffleButton.setOnClickListener(new OnShuffleClickListener(mServiceWrapper, this));

        ((SeekBar) mProgressBar).setOnSeekBarChangeListener(new OnSeekBarChangeListener(mServiceWrapper, this));

        mCurrentTime = (TextView) v.findViewById(R.id.current_time);
        mTotalTime = (TextView) v.findViewById(R.id.total_time);

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

    @Override
    public void onStart() {
        super.onStart();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_playback;
    }

    @Override
    protected boolean refreshPlaybackInfoViews() {
        boolean isSuperViewsUpdated = super.refreshPlaybackInfoViews();
        long duration = mServiceWrapper.getTrackDuration();

        if ((!isSuperViewsUpdated) || (duration <= 0)) {
            return false;
        }
        mTotalTime.setText(TimeUtils.makeTimeString(getActivity(),
                duration / OnSeekBarChangeListener.SEEK_BAR_MAX));
        return true;
    }

    @Override
    protected void refreshViewsOnError() {
        super.refreshViewsOnError();
        mTotalTime.setText("--:--");
        mCurrentTime.setText("--:--");
    }

    @Override
    protected long refreshRepeatedlyUpdateableViews() {
        long position = mServiceWrapper.getPlayingPosition();
        long duration = mServiceWrapper.getTrackDuration();
        long refreshTime = super.refreshRepeatedlyUpdateableViews();

        if ((position < 0) || (duration <= 0) ||
                (refreshTime == ERROR_REFRESH_DELAY_IN_MILLIS)) {
            return DEFAULT_REFRESH_DELAY_IN_MILLIS;
        }
        mCurrentTime.setText(TimeUtils.makeTimeString(getActivity(),
                position / OnSeekBarChangeListener.SEEK_BAR_MAX));
        return super.refreshRepeatedlyUpdateableViews();
    }

    @Override
    public void ServiceConnected() {
        super.ServiceConnected();
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
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
        refreshRepeatedlyUpdateableViews();
    }
}
