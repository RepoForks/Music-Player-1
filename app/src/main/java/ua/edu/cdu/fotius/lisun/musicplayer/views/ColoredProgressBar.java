package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ColoredProgressBar extends ProgressBar{

    public ColoredProgressBar(Context context) {
        super(context);
        setColor();
    }

    public ColoredProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColor();
    }

    public ColoredProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setColor();
    }

    private void setColor() {
        getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
    }
}
