package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class ImageMemoryCache {

    private final String TAG = getClass().getSimpleName();

    private BitmapLruCache mCache;

    public ImageMemoryCache() {
        init();
    }

    private void init() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //1/8th part of the available memory
        mCache = new BitmapLruCache(maxMemory / 8);
    }

    public void addBitmap(String key, Bitmap bitmap) {
        if((key == null) || (bitmap == null)) {
            return;
        }

        if(getBitmap(key) == null) {
            mCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmap(String key) {
        if(key == null) {
            return null;
        }
        return mCache.get(key);
    }
}
