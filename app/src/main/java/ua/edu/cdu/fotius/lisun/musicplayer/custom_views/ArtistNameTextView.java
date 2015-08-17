package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.util.AttributeSet;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ArtistNameTextView extends BaseNameTextView{

    public ArtistNameTextView(Context context) {
        super(context);
    }

    public ArtistNameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArtistNameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getUnknownName() {
        return getContext().getResources().getString(R.string.unknown_artist);
    }
}
