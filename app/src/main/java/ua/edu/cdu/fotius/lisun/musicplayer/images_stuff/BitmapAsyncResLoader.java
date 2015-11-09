package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class BitmapAsyncResLoader extends BaseBitmapAsyncLoader{

    private Resources mResources;

    public BitmapAsyncResLoader(Resources resources, ImageView imageView, ImageMemoryCache imageMemoryCache) {
        super(imageView, imageMemoryCache);
        mResources = resources;
    }

    @Override
    protected Bitmap decodeBitmap(Object bitmapSource, int width, int height) {
        mData = bitmapSource;
        return ImageUtils.decodeSampledBitmapFromResource(mResources, (Integer) mData, width, height);
    }

    @Override
    protected void addToMemoryCache(Object bitmapSource, Bitmap bitmap) {
        String resId = String.valueOf(bitmapSource);
        mImageMemoryCache.addBitmap(resId, bitmap);
    }
}
