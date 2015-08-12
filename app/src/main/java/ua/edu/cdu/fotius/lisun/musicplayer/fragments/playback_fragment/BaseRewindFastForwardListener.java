package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.RepeatingImageButton;

public abstract class BaseRewindFastForwardListener implements RepeatingImageButton.RepeatListener{

    interface RewindForwardListenerCallbacks {
        public long getPlayingPosition();
        public long getTrackDuration();
        public void goToNextTrack();
        public void goToPreviousTrack();
        public void seek(long position);
        public long refreshSeekBarAndCurrentTime();
    }

    private long mStartSeekPos = 0;
    private RewindForwardListenerCallbacks mCallbacks;

    public BaseRewindFastForwardListener(RewindForwardListenerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onRepeat(View v, long duration, int repeatCount) {
        scan(duration, repeatCount);
    }

    public abstract void scan(long howLongWasPressed, int repeatCount);

    public void setStartSeekPos() {
        this.mStartSeekPos = mCallbacks.getPlayingPosition();
    }

    public void setStartSeekPos(long mStartSeekPos) {
        this.mStartSeekPos = mStartSeekPos;
    }

    protected long getTrackDuration() {
        return mCallbacks.getTrackDuration();
    }

    public long getStartSeekPos() {
        return mStartSeekPos;
    }

    public void goToNextTrack() {
        mCallbacks.goToNextTrack();
    }

    protected void goToPreviousTrack() {
        mCallbacks.goToPreviousTrack();
    }

    protected void seek(long position) {
        mCallbacks.seek(position);
    }

    protected void refreshDependentViews() {
        mCallbacks.refreshSeekBarAndCurrentTime();
    }

    protected long getSeekLeapDelta(long howLongWasPressed) {
        if (howLongWasPressed < 5000) {
            // seek at 10x speed for the first 5 seconds
            return (howLongWasPressed * 10);
        } else {
            // seek at 40x after that
            return (50000 /*5000 millis in 10x speed*/
                    + (howLongWasPressed - 5000) * 40);
        }
    }
}
