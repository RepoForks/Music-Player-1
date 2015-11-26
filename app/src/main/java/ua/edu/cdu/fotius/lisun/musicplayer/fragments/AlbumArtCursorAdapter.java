package ua.edu.cdu.fotius.lisun.musicplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;

public class AlbumArtCursorAdapter extends BaseSimpleCursorAdapter{

    private final String TAG = getClass().getSimpleName();

    private String mAlbumIDColumnName;
    private int mAlbumArtViewResourceId;
    private ImageLoader mImageLoader;

    public AlbumArtCursorAdapter(Context context, int rowLayout, String[] from, int[] to, int albumArtViewResId, String albumIDColumnName) {
        super(context, rowLayout, from, to);
        mAlbumIDColumnName = albumIDColumnName;
        mAlbumArtViewResourceId = albumArtViewResId;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public void bindView(View rowLayout, Context context, Cursor cursor) {
        View v = rowLayout.findViewById(mAlbumArtViewResourceId);
        if(v != null) {
            //TODO : cursor == null
            int albumIdIdx = cursor.getColumnIndexOrThrow(mAlbumIDColumnName);
            long albumId = cursor.getLong(albumIdIdx);
            String filePath = DatabaseUtils.queryAlbumArtPath(mContext, albumId);
            mImageLoader.load(filePath).withDefault(R.mipmap.default_album_art_512dp).into((ImageView) v);
        }
        super.bindView(rowLayout, context, cursor);
    }

}
