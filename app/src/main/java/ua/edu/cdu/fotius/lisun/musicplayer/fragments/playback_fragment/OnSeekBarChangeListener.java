package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.widget.SeekBar;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

    private MediaPlaybackServiceWrapper mServiceWrapper;
    private PlaybackViewsStateListener mPlaybackViewsStateListener;
    private int mSeekBarMaxValue = 1000;

    public OnSeekBarChangeListener(MediaPlaybackServiceWrapper serviceWrapper,
                                   PlaybackViewsStateListener playbackViewsStateListener, int seekBarMaxValue) {
        mServiceWrapper = serviceWrapper;
        mSeekBarMaxValue = seekBarMaxValue;
        mPlaybackViewsStateListener = playbackViewsStateListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            long duration = mServiceWrapper.getTrackDuration();
            if(duration > 0) {
                long newSeekPostion = duration * progress / mSeekBarMaxValue;
                mServiceWrapper.seek(newSeekPostion);
                mPlaybackViewsStateListener.updateSeekBarAndCurrentTime();
            } else {
                //TODO: I think better will be finish app and let user restart it
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
