package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ConcealableMediaButtonsLayout extends LinearLayout implements ConcealableViewBehaviour{

    public ConcealableMediaButtonsLayout(Context context) {
        super(context);
    }

    public ConcealableMediaButtonsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConcealableMediaButtonsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedVisibilityState ss = new SavedVisibilityState(superState);
        ss.visibility = getVisibility();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedVisibilityState ss = (SavedVisibilityState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setVisibility(ss.visibility);
    }

    @Override
    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(View.GONE);
    }
}
