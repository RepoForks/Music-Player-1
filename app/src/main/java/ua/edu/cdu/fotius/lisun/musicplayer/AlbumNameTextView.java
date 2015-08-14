package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.Context;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.TextView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class AlbumNameTextView extends BaseNameTextView{

    public AlbumNameTextView(Context context) {
        super(context);
    }

    public AlbumNameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumNameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getUnknownName() {
        return getContext().getResources().getString(R.string.unknown_album);
    }
}
