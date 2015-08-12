package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

public class FastForwardListener extends BaseRewindFastForwardListener {

    public FastForwardListener(RewindForwardListenerCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void scan(long howLongWasPressed, int repeatCount) {
        if (repeatCount == 0) {
            setStartSeekPos();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = getStartSeekPos() + seekLeapTime;
            long duration = getTrackDuration();
            if (newPosition >= duration) {
                // move to next track
                goToNextTrack();
                newPosition -= duration;
                //mStartSeekPos -= duration;
                setStartSeekPos(getStartSeekPos() - duration); // is OK to go negative
            }
            seek(newPosition);
            refreshDependentViews();
        }
    }
}
