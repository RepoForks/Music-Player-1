package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ua.edu.cdu.fotius.lisun.musicplayer.DatabaseUtils;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;

public class AlbumArtCursorAdapter extends BaseSimpleCursorAdapter{

    private final String TAG = getClass().getSimpleName();

    private String mAlbumIDColumnName;

    public AlbumArtCursorAdapter(Context context, int rowLayout, String[] from, int[] to, String albumIDColumnName) {
        super(context, rowLayout, from, to);
        mAlbumIDColumnName = albumIDColumnName;
    }

    @Override
    public void bindView(View rowLayout, Context context, Cursor cursor) {
        View v = rowLayout.findViewById(R.id.album_art);
        if(v != null) {
            int albumIdIdx = cursor.getColumnIndexOrThrow(mAlbumIDColumnName);
            long albumId = cursor.getLong(albumIdIdx);
            String filePath = DatabaseUtils.queryAlbumArtPath(mContext, albumId);

//            Log.d(TAG, "bindView. Measure width: " + v.getMeasuredWidth() + " " + v.getMeasuredHeight());

            ImageLoader.from(mContext).load(filePath).into((ImageView) v);
        }
        super.bindView(rowLayout, context, cursor);
    }

}
