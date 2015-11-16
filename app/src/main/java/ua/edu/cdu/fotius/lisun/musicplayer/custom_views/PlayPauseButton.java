package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class PlayPauseButton extends ImageButton{

    private final String TAG = getClass().getSimpleName();

    private int mPlayButtonResId = 0;
    private int mPauseButtonResId = 0;

    public PlayPauseButton(Context context) {
        super(context);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    private void initAttributes(Context context, AttributeSet attrs) {

        Log.d(TAG, "1) initAttributes");

        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.PlayPauseButton, 0, 0);

        Log.d(TAG, "2) initAttributes");

        try {

            Log.d(TAG, "3) initAttributes");

            setPlayButtonResId(a.getResourceId(R.styleable.PlayPauseButton_play_src, 0));
            setPauseButtonResId(a.getResourceId(R.styleable.PlayPauseButton_pause_src, 0));
        } finally {
            a.recycle();
        }
    }

    public void setPlayButtonResId(int playButtonResId) {
        mPlayButtonResId = playButtonResId;
    }

    public void setPauseButtonResId(int pauseButtonResId) {
        mPauseButtonResId = pauseButtonResId;
    }

    public void changeStateImage(boolean isPlaying) {
        if(mPlayButtonResId == 0) {
            throw new IllegalStateException("Play button resource " +
                    "id must be set before calling this method");
        }

        if(mPauseButtonResId == 0) {
            throw new IllegalStateException("Pause button resource " +
                    "id must be set before calling this method");
        }

        if (isPlaying) {
            setImageResource(mPauseButtonResId);
        } else {
            setImageResource(mPlayButtonResId);
        }
    }
}
