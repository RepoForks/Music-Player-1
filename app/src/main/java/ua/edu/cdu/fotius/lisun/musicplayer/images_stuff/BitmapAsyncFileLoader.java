package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class BitmapAsyncFileLoader extends BaseBitmapAsyncLoader {

    public BitmapAsyncFileLoader(ImageView imageView, ImageMemoryCache imageMemoryCache) {
        super(imageView, imageMemoryCache);
    }

    @Override
    protected Bitmap decodeBitmap(Object bitmapSource, int width, int height) {
        mData = bitmapSource;
        return ImageUtils.decodeSampledBitmapFromPath((String)mData, width, height);
    }

    @Override
    protected void addToMemoryCache(Object bitmapSource, Bitmap bitmap, int imageWidth, int imageHeight) {
        mImageMemoryCache.addBitmap(ImageMemoryCache.formKey(bitmapSource, imageWidth, imageHeight), bitmap);
    }
}
