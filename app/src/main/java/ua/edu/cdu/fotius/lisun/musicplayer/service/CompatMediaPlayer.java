package ua.edu.cdu.fotius.lisun.musicplayer.service;

import android.media.MediaPlayer;
import android.os.SystemClock;

/**
 * Compatible between API levels {@link android.media.MediaPlayer}'s subclass
 * The problem with {@link android.media.MediaPlayer} differently handle
 * setting of next player
 */
public class CompatMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

    private boolean mCompatMode = true;
    private MediaPlayer mNextPlayer;
    private OnCompletionListener mCompletion;

    public CompatMediaPlayer() {
        //run in <16 API level or not
        try {
            MediaPlayer.class.getMethod("setNextMediaPlayer", MediaPlayer.class);
            mCompatMode = false;
        } catch (NoSuchMethodException e) {
            mCompatMode = true;
            super.setOnCompletionListener(this);
        }
    }

    public void setNextMediaPlayer(MediaPlayer next) {
        if (mCompatMode) {
            mNextPlayer = next;
        } else {
            super.setNextMediaPlayer(next);
        }
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        if (mCompatMode) {
            mCompletion = listener;
        } else {
            super.setOnCompletionListener(listener);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mNextPlayer != null) {
            // as it turns out, starting a new MediaPlayer on the completion
            // of a previous player ends up slightly overlapping the two
            // playbacks, so slightly delaying the start of the next player
            // gives a better user experience
            SystemClock.sleep(50);
            mNextPlayer.start();
        }
        mCompletion.onCompletion(this);
    }
}
