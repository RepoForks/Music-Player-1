package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

public class BitmapAsyncResLoader extends BaseBitmapAsyncLoader{

    private final String TAG = getClass().getSimpleName();

    private Resources mResources;

    public BitmapAsyncResLoader(Resources resources, ImageViewForLoader imageView, ImageMemoryCache imageMemoryCache) {
        super(imageView, imageMemoryCache);
        mResources = resources;
    }

    @Override
    protected Bitmap decodeBitmap(Object bitmapSource, int width, int height) {
        Log.e(TAG, "width: " + width);
        Log.e(TAG, "height: " + height);
        mData = bitmapSource;
        return ImageUtils.decodeSampledBitmapFromResource(mResources, (Integer) mData, width, height);
    }
}
