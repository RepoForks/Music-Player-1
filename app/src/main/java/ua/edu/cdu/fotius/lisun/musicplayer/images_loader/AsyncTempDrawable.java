package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

public class AsyncTempDrawable extends BitmapDrawable {

    private WeakReference<BaseBitmapAsyncLoader> mBitmapAsyncLoaderWeakReference;

    public AsyncTempDrawable(BaseBitmapAsyncLoader bitmapAsyncFileLoader) {
        mBitmapAsyncLoaderWeakReference =
                new WeakReference<BaseBitmapAsyncLoader>(bitmapAsyncFileLoader);
    }

    public BaseBitmapAsyncLoader getBitmapAsyncLoader() {
        return mBitmapAsyncLoaderWeakReference.get();
    }
}
