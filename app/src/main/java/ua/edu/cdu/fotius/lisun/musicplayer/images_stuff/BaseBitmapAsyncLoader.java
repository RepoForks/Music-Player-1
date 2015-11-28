package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public abstract class BaseBitmapAsyncLoader extends AsyncTask<Object, Void, Bitmap>{

    private final String TAG = getClass().getSimpleName();

    private WeakReference<ImageView> mImageViewWeakReference;
    protected ImageMemoryCache mImageMemoryCache = null;
    protected Object mData = null;

    public BaseBitmapAsyncLoader(ImageView imageView, ImageMemoryCache imageMemoryCache) {
        mImageViewWeakReference = new WeakReference<ImageView>(imageView);
        mImageMemoryCache  = imageMemoryCache;
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        Object bitmapSource = params[0];
        ImageView imageView = mImageViewWeakReference.get();
        Bitmap bitmap = null;
        if (imageView != null) {
            bitmap = decodeBitmap(bitmapSource, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
            addToMemoryCache(bitmapSource, bitmap, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        }
        return bitmap;
    }

    protected void addToMemoryCache(Object bitmapSource, Bitmap bitmap, int imageViewWidth, int imageViewHeight) {
        mImageMemoryCache.addBitmap(ImageMemoryCache.formKey(mData, imageViewWidth, imageViewHeight), bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = mImageViewWeakReference.get();
        if(imageView != null) {
            BaseBitmapAsyncLoader bitmapAsyncFileLoader = ImageUtils.retreiveAsyncLoader(imageView);
            if((this == bitmapAsyncFileLoader) && (bitmap != null)) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public Object getData() {
        return mData;
    }

    protected abstract Bitmap decodeBitmap(Object bitmapSource, int width, int height);
}
