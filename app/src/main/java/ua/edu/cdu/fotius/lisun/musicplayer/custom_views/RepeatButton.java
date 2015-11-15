package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.Playlist;

public class RepeatButton extends ImageButton{

    public RepeatButton(Context context) {
        super(context);
    }

    public RepeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RepeatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(int repeatMode) {
        switch (repeatMode) {
            case Playlist.REPEAT_ALL:
                setImageResource(R.mipmap.ic_repeat_all_18dp);
                break;
            case Playlist.REPEAT_CURRENT:
                setImageResource(R.mipmap.ic_repeat_one_18dp);
                break;
            case Playlist.REPEAT_NONE:
                setImageResource(R.mipmap.ic_repeat_off_18dp);
                break;
            default:
                setImageResource(R.mipmap.ic_repeat_off_18dp);
                break;
        }
    }
}
