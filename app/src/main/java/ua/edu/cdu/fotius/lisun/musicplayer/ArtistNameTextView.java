package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.Context;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.TextView;

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
