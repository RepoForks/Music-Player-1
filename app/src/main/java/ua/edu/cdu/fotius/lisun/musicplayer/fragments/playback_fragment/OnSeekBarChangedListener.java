package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.widget.SeekBar;

public class OnSeekBarChangedListener implements SeekBar.OnSeekBarChangeListener{

    public interface OnSeekBarChangedCallbacks {
        public long getTrackDuration();
        public void seek(long position);
        public long refreshSeekBarAndCurrentTime();
    }

    private OnSeekBarChangedCallbacks mCallbacks;
    private int mSeekBarMaxValue = 1000;

    public OnSeekBarChangedListener(OnSeekBarChangedCallbacks callbacks, int seekBarMaxValue) {
        mCallbacks = callbacks;
        mSeekBarMaxValue = seekBarMaxValue;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            long duration = mCallbacks.getTrackDuration();
            if(duration > 0) {
                long newSeekPostion = duration * progress / mSeekBarMaxValue;
                mCallbacks.seek(newSeekPostion);
                mCallbacks.refreshSeekBarAndCurrentTime();
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