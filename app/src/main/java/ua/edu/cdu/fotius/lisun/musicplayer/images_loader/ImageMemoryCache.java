package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageMemoryCache {

    private final String TAG = getClass().getSimpleName();

    public static ImageMemoryCache mInstance = null;

    public static ImageMemoryCache getImageMemoryCache() {
        if(mInstance == null) {
            mInstance = new ImageMemoryCache();
        }
        return mInstance;
    }

    public static String formKey(Object imageSource, int imageWidth, int imageHeight) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.valueOf(imageSource));
        stringBuffer.append(imageWidth);
        stringBuffer.append(imageHeight);
        return stringBuffer.toString();
    }

    private BitmapLruCache mCache;

    private ImageMemoryCache() {
        init();
    }

    private void init() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //1/8th part of the available memory
        mCache = new BitmapLruCache(maxMemory / 8);
    }

    public synchronized void addBitmap(String key, Bitmap bitmap) {
        if((getBitmap(key) == null) && (bitmap != null)) {
            //Log.e(TAG, "Putting in cache: " + bitmap);
            mCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmap(String key) {
        //Log.d(TAG, "Getting from cache: " + mCache.get(key));
        return mCache.get(key);
    }
}
