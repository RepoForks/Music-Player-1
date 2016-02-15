package ua.edu.cdu.fotius.lisun.musicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class AlbumArtGridViewItem extends RelativeLayout{
    public AlbumArtGridViewItem(Context context) {
        super(context);
    }

    public AlbumArtGridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumArtGridViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
