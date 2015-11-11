package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.Playlist;

public class ShuffleButton extends ImageButton{

    public ShuffleButton(Context context) {
        super(context);
    }

    public ShuffleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShuffleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(int shuffleMode) {
        switch (shuffleMode) {
            case Playlist.SHUFFLE_NONE:
                setImageResource(R.mipmap.ic_shuffle_off_btn);
                break;
            case Playlist.SHUFFLE_AUTO:
                setImageResource(R.mipmap.ic_partyshuffle_on_btn);
                break;
            case Playlist.SHUFFLE_NORMAL:
                setImageResource(R.mipmap.ic_shuffle_on_btn);
                break;
            default:
                setImageResource(R.mipmap.ic_shuffle_off_btn);
                break;
        }
    }
}
