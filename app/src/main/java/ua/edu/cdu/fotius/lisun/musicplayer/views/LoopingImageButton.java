package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * A button that will repeatedly call a 'listener' method
 * as long as the button is pressed.
 */
public class LoopingImageButton extends ImageButton {

    private long mStartTime;
    private int mRepeatCount;
    private RepeatListener mListener;
    private long mInterval = 260;
    
    public LoopingImageButton(Context context) {
        this(context, null);
    }

    public LoopingImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.imageButtonStyle);
    }

    public LoopingImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setLongClickable(true);
    }
    
    /**
     * Sets the listener to be called while the button is pressed.
     * @param l The listener that will be called
     */
    public void setRepeatListener(RepeatListener l) {
        mListener = l;
    }
    
    @Override
    public boolean performLongClick() {
        mStartTime = SystemClock.elapsedRealtime();
        mRepeatCount = 0;
        post(mRepeater);
        return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // remove the repeater, but call
            // the hook one more time
            removeCallbacks(mRepeater);
            if (mStartTime != 0) {
                doRepeat(true);
                mStartTime = 0;
            }
        }
        return super.onTouchEvent(event);
    }

    private Runnable mRepeater = new Runnable() {
        public void run() {
            doRepeat(false);
            //here we are looping
            //next time when the delayed mRepeater
            //will be called and state will be isPressed()
            //we will do one more delay and so on
            if (isPressed()) {
                postDelayed(this, mInterval);
            }
        }
    };

    private  void doRepeat(boolean last) {
        long now = SystemClock.elapsedRealtime();
        if (mListener != null) {
            mListener.onRepeat(this, now - mStartTime, last ? -1 : mRepeatCount++);
        }
    }
    
    public interface RepeatListener {
        /**
         * 0
         * specified in setRepeatListener(), for as long as the button
         * is pressed.
         * @param v The button as a View.
         * @param duration The number of milliseconds the button has been pressed so far.
         * @param repeatCount The number of previous calls in this sequence.
         * If this is going to be the last call in this sequence (i.e. the user
         * just stopped pressing the button), the value will be -1.  
         */
        void onRepeat(View v, long duration, int repeatCount);
    }
}
