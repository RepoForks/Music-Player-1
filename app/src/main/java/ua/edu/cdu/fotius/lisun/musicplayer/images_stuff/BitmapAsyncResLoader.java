package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class BitmapAsyncResLoader extends BaseBitmapAsyncLoader{

    private Resources mResources;

    public BitmapAsyncResLoader(Resources resources, ImageViewForLoader imageView, ImageMemoryCache imageMemoryCache) {
        super(imageView, imageMemoryCache);
        mResources = resources;
    }

    @Override
    protected Bitmap decodeBitmap(Object bitmapSource, int width, int height) {
        mData = bitmapSource;
        return ImageUtils.decodeSampledBitmapFromResource(mResources, (Integer) mData, width, height);
    }
}
