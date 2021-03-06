package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.service.MediaPlaybackServiceWrapper;
import ua.edu.cdu.fotius.lisun.musicplayer.views.LoopingImageButton;

public abstract class BaseRewindFastForwardListener implements LoopingImageButton.RepeatListener{

    protected long mStartSeekPos = 0;
    protected MediaPlaybackServiceWrapper mServiceWrapper;

    public BaseRewindFastForwardListener(MediaPlaybackServiceWrapper serviceWrapper) {
        mServiceWrapper = serviceWrapper;
    }

    @Override
    public void onRepeat(View v, long duration, int repeatCount) {
        scan(duration, repeatCount);
    }

    public abstract void scan(long howLongWasPressed, int repeatCount);

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
