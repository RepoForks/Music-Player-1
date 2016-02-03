package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

public class AsyncTempDrawable extends BitmapDrawable {

    private WeakReference<AlbumArtAsyncLoader> mBitmapAsyncLoaderWeakReference;

    public AsyncTempDrawable(AlbumArtAsyncLoader bitmapAsyncFileLoader) {
        mBitmapAsyncLoaderWeakReference =
                new WeakReference<AlbumArtAsyncLoader>(bitmapAsyncFileLoader);
    }

    public AlbumArtAsyncLoader getBitmapAsyncLoader() {
        return mBitmapAsyncLoaderWeakReference.get();
    }
}
