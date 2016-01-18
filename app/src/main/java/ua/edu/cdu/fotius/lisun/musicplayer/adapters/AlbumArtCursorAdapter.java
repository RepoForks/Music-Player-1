package ua.edu.cdu.fotius.lisun.musicplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.images_loader.ImageViewForLoader;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.images_loader.ImageLoader;

public class AlbumArtCursorAdapter extends IndicatorCursorAdapter {

    private String mAlbumIDColumn;
    private int mAlbumArtViewResourceId;
    private ImageLoader mImageLoader;

    public AlbumArtCursorAdapter(Context context, int rowLayout, String[] from, int[] to,
                                 int albumArtViewResId, String albumIDColumn) {
        super(context, rowLayout, from, to);
        mAlbumIDColumn = albumIDColumn;
        mAlbumArtViewResourceId = albumArtViewResId;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public void bindView(View rowLayout, Context context, Cursor cursor) {
        View v = rowLayout.findViewById(mAlbumArtViewResourceId);
        if(v != null) {
            //TODO : cursor == null
            int albumIdIdx = cursor.getColumnIndexOrThrow(mAlbumIDColumn);
            long albumId = cursor.getLong(albumIdIdx);
            mImageLoader.load(albumId).withDefault(R.drawable.default_album_art_512dp)
                    .into((ImageViewForLoader) v);
        }
        super.bindView(rowLayout, context, cursor);
    }

}
