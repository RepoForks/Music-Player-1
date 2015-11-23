package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class OnRewindListener extends BaseRewindFastForwardListener {

    public OnRewindListener(MediaPlaybackServiceWrapper serviceWrapper, PlaybackViewsStateListener playbackViewsStateListener) {
        super(serviceWrapper, playbackViewsStateListener);
    }

    @Override
    public void scan(long howLongWasPressed, int repeatCount) {
        if (repeatCount == 0) {
            mStartSeekPos = mServiceWrapper.getPlayingPosition();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = mStartSeekPos - seekLeapTime;
            if (newPosition <= 0) {
                // move to previous track
                mServiceWrapper.prev();
                newPosition = 0;
            }
            mServiceWrapper.seek(newPosition);
            mPlaybackViewsStateListener.updateSeekBarAndCurrentTime();
        }
    }
}
