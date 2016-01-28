package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service.Playlist;

public class ShuffleButton extends ImageButton {

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
        if((shuffleMode == Playlist.SHUFFLE_AUTO) || (shuffleMode == Playlist.SHUFFLE_NORMAL)) {
            setColorFilter(getResources().getColor(R.color.primary));
        } else {
            setColorFilter(getResources().getColor(R.color.secondary_text));
        }
    }
}
