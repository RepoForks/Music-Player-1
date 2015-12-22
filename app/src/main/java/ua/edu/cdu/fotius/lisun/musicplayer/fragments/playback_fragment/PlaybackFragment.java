package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity.EditTrackInfoFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackService;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.RepeatButton;
import ua.edu.cdu.fotius.lisun.musicplayer.custom_views.ShuffleButton;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.TimeUtils;

public class PlaybackFragment extends AbstractPlaybackFragment implements OnRepeatClickListener.RepeatClickedListener,
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

        mRepeatButton = (RepeatButton) v.findViewById(R.id.repeat);
        mRepeatButton.setOnClickListener(new OnRepeatClickListener(mServiceWrapper, this));

        mShuffleButton = (ShuffleButton) v.findViewById(R.id.shuffle);
        mShuffleButton.setOnClickListener(new OnShuffleClickListener(mServiceWrapper, this));

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
        mRepeatButton.setImage(mServiceWrapper.getRepeatMode());
        mShuffleButton.setImage(mServiceWrapper.getShuffleMode());
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_media_playback;
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
