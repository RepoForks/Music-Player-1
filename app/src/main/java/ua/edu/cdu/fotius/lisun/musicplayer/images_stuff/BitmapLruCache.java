package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;


import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

public class BitmapLruCache extends LruCache<String, Bitmap>{

    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return value.getByteCount() / 1024;
        } else {
            return value.getRowBytes() * value.getHeight() / 1024;
        }
    }
}
