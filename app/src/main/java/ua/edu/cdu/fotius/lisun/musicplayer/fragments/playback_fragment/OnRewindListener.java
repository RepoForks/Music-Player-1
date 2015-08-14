package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

public class OnRewindListener extends BaseRewindFastForwardListener {

    public OnRewindListener(ListenerCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void scan(long howLongWasPressed, int repeatCount) {
        if (repeatCount == 0) {
            mStartSeekPos = mCallbacks.getPlayingPosition();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = mStartSeekPos - seekLeapTime;
            if (newPosition <= 0) {
                // move to previous track
                mCallbacks.goToPreviousTrack();
                newPosition = 0;
            }
            mCallbacks.seek(newPosition);
            mCallbacks.refreshSeekBarAndCurrentTimeCallback();
        }
    }
}
