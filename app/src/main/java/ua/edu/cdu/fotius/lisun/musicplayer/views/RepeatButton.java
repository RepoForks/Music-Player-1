package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.service.Playlist;

public class RepeatButton extends ImageButton {

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
        int image = R.drawable.ic_repeat_white_18dp;
        int color = getResources().getColor(R.color.secondary_text);

        if(repeatMode == Playlist.REPEAT_CURRENT) {
            image = R.drawable.ic_repeat_one_white_18dp;
            color = getResources().getColor(R.color.primary);
        } else if(repeatMode == Playlist.REPEAT_ALL) {
            color = getResources().getColor(R.color.primary);
        }
        setImageResource(image);
        setColorFilter(color);
    }
}
