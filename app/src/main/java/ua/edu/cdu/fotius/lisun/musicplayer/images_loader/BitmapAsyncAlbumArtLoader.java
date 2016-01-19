package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.DatabaseUtils;

//TODO: rename this class
public class BitmapAsyncAlbumArtLoader extends BaseBitmapAsyncLoader {

    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private int mDefaultImageResId;

    public BitmapAsyncAlbumArtLoader(Context context, int defaultImageResId, ImageViewForLoader imageView, ImageMemoryCache imageMemoryCache) {
        super(imageView, imageMemoryCache);
        mContext = context;
        mDefaultImageResId = defaultImageResId;
    }

    @Override
    protected Bitmap decodeBitmap(Object albumId, int width, int height) {
        Log.e(TAG, "albumArt.width: " + width);
        Log.e(TAG, "albumArt.height: " + height);
        String albumArtPath = DatabaseUtils.queryAlbumArtPath(mContext, (Long) albumId);
        if ((albumArtPath != null) && (new File(albumArtPath).exists())) {
            mData = albumId;
            return ImageUtils.decodeSampledBitmapFromPath(albumArtPath, width, height);
        } else {
            mData = mDefaultImageResId;
            return ImageUtils.decodeSampledBitmapFromResource(mContext.getResources(), mDefaultImageResId, width, height);
        }
    }
}