package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;

public class OnRewindListener extends BaseRewindFastForwardListener {

    public interface RewindClickedListener{
        public void onRewindClicked();
    }

    private RewindClickedListener mClickedListener;

    public OnRewindListener(MediaPlaybackServiceWrapper serviceWrapper, RewindClickedListener clickedListener) {
        super(serviceWrapper);
        mClickedListener = clickedListener;
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
            mClickedListener.onRewindClicked();
        }
    }
}
