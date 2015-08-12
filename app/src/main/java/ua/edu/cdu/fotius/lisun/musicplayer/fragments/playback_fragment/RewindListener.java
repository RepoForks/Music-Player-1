package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

public class RewindListener extends BaseRewindFastForwardListener {

    public RewindListener(RewindForwardListenerCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void scan(long howLongWasPressed, int repeatCount) {
        if (repeatCount == 0) {
            setStartSeekPos();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = getStartSeekPos() - seekLeapTime;
            if (newPosition <= 0) {
                // move to previous track
                goToPreviousTrack();
                newPosition = 0;
            }
            seek(newPosition);
            refreshDependentViews();
        }
    }
}
