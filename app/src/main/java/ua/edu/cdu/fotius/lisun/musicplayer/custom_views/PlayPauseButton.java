package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class PlayPauseButton extends ImageButton{

    public PlayPauseButton(Context context) {
        super(context);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray
    }

    public void setImage(boolean isPlaying) {
        if (isPlaying) {
            setImageResource(R.drawable.media_button_pause_selector);
        } else {
            setImageResource(R.drawable.media_button_play_selector);
        }
    }
}
