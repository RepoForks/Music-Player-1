package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewForLoader extends ImageView {

    public ImageViewForLoader(Context context) {
        super(context);
    }

    public ImageViewForLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewForLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getViewWidth() {
        return getWidth();
    }

    public int getViewHeight() {
        return getHeight();
    }
}
