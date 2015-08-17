package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

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
            setImageResource(android.R.drawable.ic_media_pause);
        } else {
            setImageResource(android.R.drawable.ic_media_play);
        }
    }
}
