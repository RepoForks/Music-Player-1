package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class PlayButton extends ImageButton{

    public PlayButton(Context context) {
        super(context);
    }

    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(boolean isPlaying) {
        if (isPlaying) {
            setImageResource(R.drawable.media_button_pause_selector);
        } else {
            setImageResource(R.drawable.media_button_play_selector);
        }
    }
}
