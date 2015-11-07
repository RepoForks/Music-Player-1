package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class ImageLoader {

    private static ImageLoader instance;

    public static ImageLoader from(Context c) {
        if(instance == null) {
            instance = new ImageLoader(c);
        }
        return instance;
    }

    private String mFilePath = null;
    private ImageMemoryCache mImageMemoryCache;

    private ImageLoader(Context c){
        mImageMemoryCache = new ImageMemoryCache(c);
    }

    public ImageLoader load(String filePath) {
        mFilePath = filePath;
        return this;
    }

    public void into(ImageView imageView) {
        loadBitmap(mFilePath, imageView);
    }

    private void loadBitmap(String filePath, ImageView imageView) {
        if(cancelPotentialWork(filePath, imageView)) {
            Bitmap bitmap = mImageMemoryCache.getBitmap(filePath);
            if(bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                BitmapAsyncLoader bitmapAsyncLoader = new BitmapAsyncLoader(imageView, mImageMemoryCache);
                AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(bitmapAsyncLoader);
                imageView.setImageDrawable(asyncTempDrawable);
                bitmapAsyncLoader.execute(filePath);
            }
        }
    }

    private boolean cancelPotentialWork(String filePath, ImageView imageView) {
        BitmapAsyncLoader bitmapAsyncLoader = ImageUtils.getAsyncLoader(imageView);
        if(bitmapAsyncLoader != null) {
            String imagePath = bitmapAsyncLoader.getImagePath();
            if(imagePath == null || !imagePath.equals(mFilePath)) {
                bitmapAsyncLoader.cancel(true);
            } else {
                /*same task is running*/
                return false;
            }
        }
        return true;
    }
}
