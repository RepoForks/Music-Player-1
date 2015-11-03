package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

public class AsyncTempDrawable extends BitmapDrawable {

    private WeakReference<BitmapAsyncLoader> mBitmapAsyncLoaderWeakReference;

    public AsyncTempDrawable(BitmapAsyncLoader bitmapAsyncLoader) {
        mBitmapAsyncLoaderWeakReference =
                new WeakReference<BitmapAsyncLoader>(bitmapAsyncLoader);
    }

    public BitmapAsyncLoader getBitmapAsyncLoader() {
        return mBitmapAsyncLoaderWeakReference.get();
    }
}
