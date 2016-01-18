package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


/**
 * This class should be used as temporary fix for all layouts
 * in {@link ua.edu.cdu.fotius.lisun.musicplayer.sliding_panel.SlidingUpPanelLayout}
 */
//TODO:
// find out why unpredictable behaviour is happens in SlidingUpPanelLayout when click
// on default LinearLayout(it resets the media player)
public class NotClickableLinearLayout extends LinearLayout {



    public NotClickableLinearLayout(Context context) {
        super(context);
    }

    public NotClickableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotClickableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if touch event was performed on this layout*/
        return true;
    }
}
