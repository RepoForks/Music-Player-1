package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.widget.SeekBar;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

    public static final int SEEK_BAR_MAX = 1000;

    public interface SeekBarProgressChangedListener {
        public void onProgressChanged();
    }


    private MediaPlaybackServiceWrapper mServiceWrapper;
    private SeekBarProgressChangedListener mChangerListener;

    public OnSeekBarChangeListener(MediaPlaybackServiceWrapper serviceWrapper,
                                   SeekBarProgressChangedListener progressChangedListener) {
        mServiceWrapper = serviceWrapper;
        mChangerListener = progressChangedListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            long duration = mServiceWrapper.getTrackDuration();
            if(duration > 0) {
                long newSeekPostion = duration * progress / SEEK_BAR_MAX;
                mServiceWrapper.seek(newSeekPostion);
                mChangerListener.onProgressChanged();
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
