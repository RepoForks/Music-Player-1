package ua.edu.cdu.fotius.lisun.musicplayer.fragments.playback_fragment;

import ua.edu.cdu.fotius.lisun.musicplayer.MediaPlaybackServiceWrapper;

public class OnFastForwardListener extends BaseRewindFastForwardListener {


    public OnFastForwardListener(MediaPlaybackServiceWrapper serviceWrapper, PlaybackViewsStateListener playbackViewsStateListener) {
        super(serviceWrapper, playbackViewsStateListener);
    }

    @Override
    public void scan(long howLongWasPressed, int repeatCount) {
        if (repeatCount == 0) {
            //setStartSeekPos();
            mStartSeekPos = mServiceWrapper.getPlayingPosition();
        } else {
            long seekLeapTime = getSeekLeapDelta(howLongWasPressed);
            long newPosition = mStartSeekPos + seekLeapTime;
            long duration = mServiceWrapper.getTrackDuration();
            if (newPosition >= duration) {
                // move to next track
                mServiceWrapper.next();
                newPosition -= duration;
                mStartSeekPos -= duration; // is OK to go negative
            }
            mServiceWrapper.seek(newPosition);
            mPlaybackViewsStateListener.updateSeekBarAndCurrentTime();
        }
    }
}
