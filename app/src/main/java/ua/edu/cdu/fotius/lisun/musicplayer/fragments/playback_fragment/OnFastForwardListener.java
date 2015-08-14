package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

public class OnFastForwardListener extends BaseRewindFastForwardListener {

    public OnFastForwardListener(ListenerCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void scan(long howLongWasPressed, int repeatCount) {
        if (repeatCount == 0) {
            //setStartSeekPos();
            mStartSeekPos = mCallbacks.getPlayingPosition();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = mStartSeekPos + seekLeapTime;
            long duration = mCallbacks.getTrackDuration();
            if (newPosition >= duration) {
                // move to next track
                mCallbacks.goToNextTrack();
                newPosition -= duration;
                mStartSeekPos -= duration; // is OK to go negative
            }
            mCallbacks.seek(newPosition);
            mCallbacks.refreshSeekBarAndCurrentTimeCallback();
        }
    }
}
